package com.petcompany.platform.modules.order.service;

import com.petcompany.platform.modules.order.dto.OrderResponse;
import com.petcompany.platform.modules.order.entity.Order;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单服务接口
 */
public interface OrderService {

    /**
     * 创建订单
     */
    Order createOrder(Long userId, Long petId, Long serviceTypeId, LocalDateTime serviceStartTime, LocalDateTime serviceEndTime, String address);

    /**
     * 取消订单
     */
    void cancelOrder(Long userId, Long orderId);

    /**
     * 接受订单
     */
    void acceptOrder(Long providerId, Long orderId);

    /**
     * 开始服务
     */
    void startService(Long providerId, Long orderId);

    /**
     * 完成服务
     */
    void completeService(Long providerId, Long orderId);

    /**
     * 根据ID获取订单
     */
    Order getOrderById(Long orderId);

    /**
     * 获取用户的订单列表
     */
    List<OrderResponse> getUserOrderList(Long userId);

    /**
     * 获取服务提供者的订单列表
     */
    List<OrderResponse> getProviderOrderList(Long providerId);

}
