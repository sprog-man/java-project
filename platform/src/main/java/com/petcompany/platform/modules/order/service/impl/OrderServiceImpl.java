package com.petcompany.platform.modules.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.petcompany.platform.common.exception.BusinessException;
import com.petcompany.platform.common.result.PageResult;
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
        // 验证宠物是否存在
        Pet pet = petService.getPetById(petId);
        if (pet == null) {
            throw new BusinessException("宠物不存在");
        }

        // 验证宠物是否属于当前用户
        if (!pet.getUserId().equals(userId)) {
            throw new BusinessException("宠物不属于当前用户");
        }

        // 验证服务类型是否存在
        ServiceType serviceType = serviceTypeService.getServiceTypeById(serviceTypeId);
        if (serviceType == null) {
            throw new BusinessException("服务类型不存在");
        }

        // 计算服务时长（小时）
        long hours = serviceEndTime.toEpochSecond(ZoneOffset.of("+8")) - serviceStartTime.toEpochSecond(ZoneOffset.of("+8"));
        hours = hours / 3600;
        if (hours < 1) {
            hours = 1;
        }

        // 计算订单金额
        double price = serviceType.getBasePrice() + serviceType.getPricePerHour() * hours;

        // 创建订单
        Order order = new Order();
        order.setUserId(userId);
        order.setPetId(petId);
        order.setServiceTypeId(serviceTypeId);
        order.setServiceStartTime(serviceStartTime);
        order.setServiceEndTime(serviceEndTime);
        order.setAddress(address);
        order.setPrice(price);
        order.setStatus(1); // 待支付
        order.setOrderNo(generateOrderNo());
        order.setCreateTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());
        order.setDeleted(0);

        // 保存订单
        orderMapper.insert(order);
        log.info("用户{}创建订单{}，金额：{}", userId, order.getOrderNo(), price);

        return order;
    }

    @Override
    public void cancelOrder(Long userId, Long orderId) {
        // 获取订单
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }

        // 验证订单是否属于当前用户
        if (!order.getUserId().equals(userId)) {
            throw new BusinessException("订单不属于当前用户");
        }

        // 验证订单状态
        if (order.getStatus() != 1) { // 只有待支付状态可以取消
            throw new BusinessException("订单状态不允许取消");
        }

        // 更新订单状态
        order.setStatus(6); // 已取消
        order.setUpdateTime(LocalDateTime.now());
        orderMapper.updateById(order);
        log.info("用户{}取消订单{}", userId, orderId);
    }

    @Override
    public void acceptOrder(Long providerId, Long orderId) {
        // 获取订单
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }

        // 验证订单状态
        if (order.getStatus() != 2) { // 只有待接单状态可以接受
            throw new BusinessException("订单状态不允许接受");
        }

        // 更新订单状态
        order.setStatus(3); // 服务中
        order.setProviderId(providerId);
        order.setUpdateTime(LocalDateTime.now());
        orderMapper.updateById(order);
        log.info("服务提供者{}接受订单{}", providerId, orderId);
    }

    @Override
    public void startService(Long providerId, Long orderId) {
        // 获取订单
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }

        // 验证订单是否属于当前服务提供者
        if (!order.getProviderId().equals(providerId)) {
            throw new BusinessException("订单不属于当前服务提供者");
        }

        // 验证订单状态
        if (order.getStatus() != 3) { // 只有服务中状态可以开始服务
            throw new BusinessException("订单状态不允许开始服务");
        }

        // 更新订单状态
        order.setStatus(4); // 服务中
        order.setUpdateTime(LocalDateTime.now());
        orderMapper.updateById(order);
        log.info("服务提供者{}开始服务{}", providerId, orderId);
    }

    @Override
    public void completeService(Long providerId, Long orderId) {
        // 获取订单
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }

        // 验证订单是否属于当前服务提供者
        if (!order.getProviderId().equals(providerId)) {
            throw new BusinessException("订单不属于当前服务提供者");
        }

        // 验证订单状态
        if (order.getStatus() != 4) { // 只有服务中状态可以完成服务
            throw new BusinessException("订单状态不允许完成服务");
        }

        // 更新订单状态
        order.setStatus(5); // 待评价
        order.setUpdateTime(LocalDateTime.now());
        orderMapper.updateById(order);
        log.info("服务提供者{}完成服务{}", providerId, orderId);
    }

    @Override
    public Order getOrderById(Long orderId) {
        return orderMapper.selectById(orderId);
    }

    @Override
    public List<OrderResponse> getUserOrderList(Long userId) {
        // 查询用户订单列表
        LambdaQueryWrapper<Order> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Order::getUserId, userId);
        wrapper.eq(Order::getDeleted, 0);
        wrapper.orderByDesc(Order::getCreateTime);

        List<Order> orders = orderMapper.selectList(wrapper);
        return orders.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    @Override
    public PageResult<OrderResponse> getUserOrderListPage(Long userId, Integer page, Integer size) {
        // 查询用户订单列表
        LambdaQueryWrapper<Order> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Order::getUserId, userId);
        wrapper.eq(Order::getDeleted, 0);
        wrapper.orderByDesc(Order::getCreateTime);

        // 计算总条数
        long total = orderMapper.selectCount(wrapper);

        // 分页查询
        int offset = (page - 1) * size;
        wrapper.last("LIMIT " + offset + ", " + size);

        List<Order> orders = orderMapper.selectList(wrapper);
        List<OrderResponse> responses = orders.stream().map(this::convertToResponse).collect(Collectors.toList());

        // 构建分页结果
        PageResult<OrderResponse> result = new PageResult<>(responses, total, size, page);

        return result;
    }

    @Override
    public List<OrderResponse> getProviderOrderList(Long providerId) {
        // 查询服务提供者订单列表
        LambdaQueryWrapper<Order> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Order::getProviderId, providerId);
        wrapper.eq(Order::getDeleted, 0);
        wrapper.orderByDesc(Order::getCreateTime);

        List<Order> orders = orderMapper.selectList(wrapper);
        return orders.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    @Override
    public PageResult<OrderResponse> getProviderOrderListPage(Long providerId, Integer page, Integer size) {
        // 查询服务提供者订单列表
        LambdaQueryWrapper<Order> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Order::getProviderId, providerId);
        wrapper.eq(Order::getDeleted, 0);
        wrapper.orderByDesc(Order::getCreateTime);

        // 计算总条数
        long total = orderMapper.selectCount(wrapper);

        // 分页查询
        int offset = (page - 1) * size;
        wrapper.last("LIMIT " + offset + ", " + size);

        List<Order> orders = orderMapper.selectList(wrapper);
        List<OrderResponse> responses = orders.stream().map(this::convertToResponse).collect(Collectors.toList());

        // 构建分页结果
        PageResult<OrderResponse> result = new PageResult<>(responses, total, size, page);

        return result;
    }

    @Override
    public List<OrderResponse> getPendingOrderList() {
        // 查询待接单的订单列表
        LambdaQueryWrapper<Order> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Order::getStatus, 2); // 待接单
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
     * 转换为响应对象
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
        response.setCreateTime(order.getCreateTime());

        // 获取宠物信息
        Pet pet = petService.getPetById(order.getPetId());
        if (pet != null) {
            response.setPetName(pet.getName());
        }

        // 获取服务类型信息
        ServiceType serviceType = serviceTypeService.getServiceTypeById(order.getServiceTypeId());
        if (serviceType != null) {
            response.setServiceTypeName(serviceType.getName());
        }

        return response;
    }

}
