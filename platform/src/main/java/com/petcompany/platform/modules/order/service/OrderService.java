package com.petcompany.platform.modules.order.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.petcompany.platform.modules.order.dto.OrderCreateRequest;
import com.petcompany.platform.modules.order.dto.OrderResponse;
import com.petcompany.platform.modules.order.entity.Order;

import java.util.List;

/**
 * 订单服务接口
 */
public interface OrderService {

    /**
     * 获取订单列表
     */
    List<OrderResponse> getOrderList();

    /**
     * 获取订单详情
     */
    OrderResponse getOrderDetail(Long id);

    /**
     * 获取订单实体
     */
    Order getOrderById(Long id);

    /**
     * 创建订单
     */
    OrderResponse createOrder(OrderCreateRequest request);

    /**
     * 取消订单
     */
    OrderResponse cancelOrder(Long id);

    /**
     * 接受订单
     */
    OrderResponse acceptOrder(Long id);

    /**
     * 开始服务
     */
    OrderResponse startOrder(Long id);

    /**
     * 完成服务
     */
    OrderResponse completeOrder(Long id);

    /**
     * 获取服务提供者订单列表
     */
    List<OrderResponse> getProviderOrderList();

    /**
     * 获取待接单订单列表
     */
    List<OrderResponse> getPendingOrderList();



    /**
     * 统计指定服务提供者在特定状态下的订单数量
     */
    Long countByProviderIdAndStatus(Long providerId, Integer status);


    /*
    * 通过用户ID获取订单列表
    * */
    List<OrderResponse> getOrderListByUserId(Long userId);

    /*
    * 管理员获取订单列表
    * */
    public Page<OrderResponse> getAdminOrderPage(int page, int size, Integer status, String keyword);

}

