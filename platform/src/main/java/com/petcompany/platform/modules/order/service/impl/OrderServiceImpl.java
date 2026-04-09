package com.petcompany.platform.modules.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.petcompany.platform.common.exception.BusinessException;
import com.petcompany.platform.common.service.AsyncService;
import com.petcompany.platform.modules.order.dto.OrderCreateRequest;
import com.petcompany.platform.modules.order.dto.OrderResponse;
import com.petcompany.platform.modules.order.entity.Order;
import com.petcompany.platform.modules.order.service.OrderService;
import com.petcompany.platform.modules.order.mapper.OrderMapper;
import com.petcompany.platform.modules.pet.entity.Pet;
import com.petcompany.platform.modules.pet.service.PetService;
import com.petcompany.platform.modules.service.entity.ServiceType;
import com.petcompany.platform.modules.service.service.ServiceTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private AsyncService asyncService;

    @Override
    public List<OrderResponse> getOrderList() {
        // 查询用户订单列表
        LambdaQueryWrapper<Order> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Order::getDeleted, 0);
        wrapper.orderByDesc(Order::getCreateTime);

        List<Order> orders = orderMapper.selectList(wrapper);
        return orders.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    @Override
    public OrderResponse getOrderDetail(Long id) {
        Order order = orderMapper.selectById(id);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        return convertToResponse(order);
    }

    @Override
    public Order getOrderById(Long id) {
        return orderMapper.selectById(id);
    }

    @Override
    public OrderResponse createOrder(OrderCreateRequest request) {
        // 验证宠物是否存在
        Pet pet = petService.getPetById(request.getPetId());
        if (pet == null) {
            throw new BusinessException("宠物不存在");
        }

        // 验证服务类型是否存在
        ServiceType serviceType = serviceTypeService.getServiceTypeById(request.getServiceType());
        if (serviceType == null) {
            throw new BusinessException("服务类型不存在");
        }

        // 解析服务时间
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime serviceStartTime = LocalDateTime.parse(request.getServiceDate() + " " + request.getServiceStartTime(), formatter);
        LocalDateTime serviceEndTime = LocalDateTime.parse(request.getServiceDate() + " " + request.getServiceEndTime(), formatter);

        // 创建订单
        Order order = new Order();
        order.setUserId(1L); // 暂时硬编码用户ID，后续从上下文获取
        order.setPetId(request.getPetId());
        order.setServiceTypeId(request.getServiceType());
        order.setServiceStartTime(serviceStartTime);
        order.setServiceEndTime(serviceEndTime);
        order.setAddress(request.getAddress());
        order.setPhone(request.getPhone());
        order.setNotes(request.getNotes());
        order.setPrice(request.getPrice().doubleValue());
        order.setStatus(1); // 待支付
        order.setPayStatus(0); // 未支付
        order.setOrderNo(generateOrderNo());
        order.setCreateTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());
        order.setDeleted(0);

        // 保存订单
        orderMapper.insert(order);
        log.info("创建订单{}，金额：{}", order.getOrderNo(), request.getPrice());

        // 异步处理订单相关的耗时操作
        asyncService.processOrder(order.getId(), 1L);
        asyncService.sendNotification(1L, "您的订单" + order.getOrderNo() + "已创建成功");

        return convertToResponse(order);
    }

    @Override
    public OrderResponse cancelOrder(Long id) {
        // 获取订单
        Order order = orderMapper.selectById(id);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }

        // 验证订单状态
        if (order.getStatus() != 1) { // 只有待支付状态可以取消
            throw new BusinessException("订单状态不允许取消");
        }

        // 更新订单状态
        order.setStatus(6); // 已取消
        order.setUpdateTime(LocalDateTime.now());
        orderMapper.updateById(order);
        log.info("取消订单{}", id);

        return convertToResponse(order);
    }

    @Override
    public OrderResponse acceptOrder(Long id) {
        // 获取订单
        Order order = orderMapper.selectById(id);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }

        // 验证订单状态
        if (order.getStatus() != 2) { // 只有待接单状态可以接受
            throw new BusinessException("订单状态不允许接受");
        }

        // 更新订单状态
        order.setStatus(3); // 服务中
        order.setProviderId(1L); // 暂时硬编码服务提供者ID
        order.setUpdateTime(LocalDateTime.now());
        orderMapper.updateById(order);
        log.info("接受订单{}", id);

        return convertToResponse(order);
    }

    @Override
    public OrderResponse startOrder(Long id) {
        // 获取订单
        Order order = orderMapper.selectById(id);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }

        // 验证订单状态
        if (order.getStatus() != 3) { // 只有服务中状态可以开始服务
            throw new BusinessException("订单状态不允许开始服务");
        }

        // 更新订单状态
        order.setStatus(4); // 服务中
        order.setUpdateTime(LocalDateTime.now());
        orderMapper.updateById(order);
        log.info("开始服务{}", id);

        return convertToResponse(order);
    }

    @Override
    public OrderResponse completeOrder(Long id) {
        // 获取订单
        Order order = orderMapper.selectById(id);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }

        // 验证订单状态
        if (order.getStatus() != 4) { // 只有服务中状态可以完成服务
            throw new BusinessException("订单状态不允许完成服务");
        }

        // 更新订单状态
        order.setStatus(5); // 待评价
        order.setUpdateTime(LocalDateTime.now());
        orderMapper.updateById(order);
        log.info("完成服务{}", id);

        return convertToResponse(order);
    }

    @Override
    public List<OrderResponse> getProviderOrderList() {
        // 查询服务提供者订单列表
        LambdaQueryWrapper<Order> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Order::getProviderId, 1L); // 暂时硬编码服务提供者ID
        wrapper.eq(Order::getDeleted, 0);
        wrapper.orderByDesc(Order::getCreateTime);

        List<Order> orders = orderMapper.selectList(wrapper);
        return orders.stream().map(this::convertToResponse).collect(Collectors.toList());
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
        response.setAddress(order.getAddress());
        response.setPhone(order.getPhone());
        response.setNotes(order.getNotes());
        response.setPrice(order.getPrice().intValue());
        response.setCreateTime(order.getCreateTime());
        response.setReviewed(false);

        // 获取宠物信息
        Pet pet = petService.getPetById(order.getPetId());
        if (pet != null) {
            response.setPetName(pet.getName());
        }

        // 获取服务类型信息
        ServiceType serviceType = serviceTypeService.getServiceTypeById(order.getServiceTypeId());
        if (serviceType != null) {
            response.setServiceName(serviceType.getName());
        }

        // 格式化服务时间
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String serviceTime = order.getServiceStartTime().format(formatter) + "-" + order.getServiceEndTime().format(formatter);
        response.setServiceTime(serviceTime);

        // 设置订单状态
        switch (order.getStatus()) {
            case 1:
                response.setStatus("PENDING_PAYMENT");
                response.setStatusText("待支付");
                break;
            case 2:
                response.setStatus("PENDING");
                response.setStatusText("待接单");
                break;
            case 3:
            case 4:
                response.setStatus("IN_SERVICE");
                response.setStatusText("服务中");
                break;
            case 5:
                response.setStatus("COMPLETED");
                response.setStatusText("已完成");
                break;
            case 6:
                response.setStatus("CANCELLED");
                response.setStatusText("已取消");
                break;
            default:
                response.setStatus("UNKNOWN");
                response.setStatusText("未知状态");
        }

        // 设置支付状态
        switch (order.getPayStatus()) {
            case 0:
                response.setPaymentStatusText("待支付");
                break;
            case 1:
                response.setPaymentStatusText("已支付");
                break;
            case 2:
                response.setPaymentStatusText("退款中");
                break;
            case 3:
                response.setPaymentStatusText("已退款");
                break;
            default:
                response.setPaymentStatusText("未知状态");
        }

        return response;
    }

}
