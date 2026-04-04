package com.petcompany.platform.modules.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.petcompany.platform.common.exception.BusinessException;
import com.petcompany.platform.modules.order.dto.OrderResponse;
import com.petcompany.platform.modules.order.entity.Order;
import com.petcompany.platform.modules.order.service.OrderService;
import com.petcompany.platform.modules.order.mapper.OrderMapper;
import com.petcompany.platform.modules.pet.entity.Pet;
import com.petcompany.platform.modules.pet.service.PetService;
import com.petcompany.platform.modules.service.entity.ServiceType;
import com.petcompany.platform.modules.service.service.ServiceTypeService;
import com.petcompany.platform.modules.user.entity.User;
import com.petcompany.platform.modules.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单服务实现类
 */
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private PetService petService;

    @Resource
    private ServiceTypeService serviceTypeService;

    @Resource
    private UserService userService;

    @Override
    public Order createOrder(Long userId, Long petId, Long serviceTypeId, LocalDateTime serviceStartTime, LocalDateTime serviceEndTime, String address) {
        // 检查宠物是否存在
        Pet pet = petService.getPetById(petId);
        if (pet == null) {
            throw new BusinessException("宠物不存在");
        }

        // 检查是否是用户自己的宠物
        if (!pet.getUserId().equals(userId)) {
            throw new BusinessException("无权操作此宠物");
        }

        // 检查服务类型是否存在
        ServiceType serviceType = serviceTypeService.getServiceTypeById(serviceTypeId);
        if (serviceType == null) {
            throw new BusinessException("服务类型不存在");
        }

        // 计算价格
        double price = calculatePrice(serviceType, serviceStartTime, serviceEndTime);

        // 创建订单
        Order order = new Order();
        order.setOrderNo(generateOrderNo());
        order.setUserId(userId);
        order.setPetId(petId);
        order.setServiceTypeId(serviceTypeId);
        order.setServiceStartTime(serviceStartTime);
        order.setServiceEndTime(serviceEndTime);
        order.setAddress(address);
        order.setPrice(price);
        order.setStatus(1); // 待支付
        order.setPayStatus(0); // 未支付
        order.setCreateTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());
        order.setDeleted(0);

        orderMapper.insert(order);
        return order;
    }

    @Override
    public void cancelOrder(Long userId, Long orderId) {
        // 检查订单是否存在
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }

        // 检查是否是用户自己的订单
        if (!order.getUserId().equals(userId)) {
            throw new BusinessException("无权操作此订单");
        }

        // 检查订单状态
        if (order.getStatus() != 1 && order.getStatus() != 2) {
            throw new BusinessException("订单状态不允许取消");
        }

        // 更新订单状态
        order.setStatus(6); // 已取消
        order.setUpdateTime(LocalDateTime.now());
        orderMapper.updateById(order);
    }

    @Override
    public void acceptOrder(Long providerId, Long orderId) {
        // 检查订单是否存在
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }

        // 检查订单状态
        if (order.getStatus() != 2) {
            throw new BusinessException("订单状态不允许接受");
        }

        // 更新订单状态
        order.setProviderId(providerId);
        order.setStatus(3); // 服务中
        order.setUpdateTime(LocalDateTime.now());
        orderMapper.updateById(order);
    }

    @Override
    public void startService(Long providerId, Long orderId) {
        // 检查订单是否存在
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }

        // 检查是否是服务提供者自己的订单
        if (!order.getProviderId().equals(providerId)) {
            throw new BusinessException("无权操作此订单");
        }

        // 检查订单状态
        if (order.getStatus() != 3) {
            throw new BusinessException("订单状态不允许开始服务");
        }

        // 更新订单状态
        order.setUpdateTime(LocalDateTime.now());
        orderMapper.updateById(order);
    }

    @Override
    public void completeService(Long providerId, Long orderId) {
        // 检查订单是否存在
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }

        // 检查是否是服务提供者自己的订单
        if (!order.getProviderId().equals(providerId)) {
            throw new BusinessException("无权操作此订单");
        }

        // 检查订单状态
        if (order.getStatus() != 3) {
            throw new BusinessException("订单状态不允许完成服务");
        }

        // 更新订单状态
        order.setStatus(4); // 待评价
        order.setUpdateTime(LocalDateTime.now());
        orderMapper.updateById(order);
    }

    @Override
    public Order getOrderById(Long orderId) {
        return orderMapper.selectById(orderId);
    }

    @Override
    public List<OrderResponse> getUserOrderList(Long userId) {
        LambdaQueryWrapper<Order> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Order::getUserId, userId);
        wrapper.eq(Order::getDeleted, 0);
        wrapper.orderByDesc(Order::getCreateTime);
        List<Order> orders = orderMapper.selectList(wrapper);

        return orders.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    @Override
    public List<OrderResponse> getProviderOrderList(Long providerId) {
        LambdaQueryWrapper<Order> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Order::getProviderId, providerId);
        wrapper.eq(Order::getDeleted, 0);
        wrapper.orderByDesc(Order::getCreateTime);
        List<Order> orders = orderMapper.selectList(wrapper);

        return orders.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    /**
     * 生成订单号
     */
    private String generateOrderNo() {
        return "ORD" + System.currentTimeMillis() + (int) (Math.random() * 1000);
    }

    /**
     * 计算价格
     */
    private double calculatePrice(ServiceType serviceType, LocalDateTime startTime, LocalDateTime endTime) {
        // 计算服务时长（小时）
        long hours = endTime.toEpochSecond(ZoneOffset.ofHours(8)) - startTime.toEpochSecond(ZoneOffset.ofHours(8));
        hours = hours / 3600;
        if (hours < 1) {
            hours = 1;
        }

        // 基础价格 + 每小时价格 * 时长
        return serviceType.getBasePrice() + serviceType.getPricePerHour() * hours;
    }

    /**
     * 转换为响应DTO
     */
    private OrderResponse convertToResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setOrderNo(order.getOrderNo());
        response.setServiceStartTime(order.getServiceStartTime());
        response.setServiceEndTime(order.getServiceEndTime());
        response.setAddress(order.getAddress());
        response.setPrice(order.getPrice());
        response.setStatus(order.getStatus());
        response.setPayStatus(order.getPayStatus());
        response.setCreateTime(order.getCreateTime());

        // 设置宠物名称
        Pet pet = petService.getPetById(order.getPetId());
        if (pet != null) {
            response.setPetName(pet.getName());
        }

        // 设置服务类型名称
        ServiceType serviceType = serviceTypeService.getServiceTypeById(order.getServiceTypeId());
        if (serviceType != null) {
            response.setServiceTypeName(serviceType.getName());
        }

        // 设置服务提供者名称
        if (order.getProviderId() != null) {
            User provider = userService.getUserById(order.getProviderId());
            if (provider != null) {
                response.setProviderName(provider.getNickname());
            }
        }

        // 设置订单状态名称
        if (order.getStatus() != null) {
            switch (order.getStatus()) {
                case 1: response.setStatusName("待支付"); break;
                case 2: response.setStatusName("待接单"); break;
                case 3: response.setStatusName("服务中"); break;
                case 4: response.setStatusName("待评价"); break;
                case 5: response.setStatusName("已完成"); break;
                case 6: response.setStatusName("已取消"); break;
                default: response.setStatusName("未知");
            }
        }

        // 设置支付状态名称
        if (order.getPayStatus() != null) {
            switch (order.getPayStatus()) {
                case 0: response.setPayStatusName("未支付"); break;
                case 1: response.setPayStatusName("已支付"); break;
                case 2: response.setPayStatusName("退款中"); break;
                case 3: response.setPayStatusName("已退款"); break;
                default: response.setPayStatusName("未知");
            }
        }

        return response;
    }

}
