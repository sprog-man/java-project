package com.petcompany.platform.modules.order.service;

import com.petcompany.platform.common.result.PageResult;
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
     * 获取订单详情
     */
    Order getOrderById(Long orderId);

    /**
     * 获取用户订单列表
     */
    List<OrderResponse> getUserOrderList(Long userId);

    /**
     * 分页获取用户订单列表
     */
    PageResult<OrderResponse> getUserOrderListPage(Long userId, Integer page, Integer size);

    /**
     * 获取服务提供者订单列表
     */
    List<OrderResponse> getProviderOrderList(Long providerId);

    /**
     * 分页获取服务提供者订单列表
     */
    PageResult<OrderResponse> getProviderOrderListPage(Long providerId, Integer page, Integer size);

    /**
     * 获取待接单的订单列表
     */
    List<OrderResponse> getPendingOrderList();

}
