package com.petcompany.platform.modules.order.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.petcompany.platform.modules.order.dto.OrderCreateRequest;
import com.petcompany.platform.modules.order.dto.OrderResponse;
import com.petcompany.platform.modules.order.entity.Order;

import java.util.List;
import java.util.Map;

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
    Page<OrderResponse> getAdminOrderPage(int page, int size, String status, Long serviceTypeId, String keyword);


    /**
     * ✅ 新增：专门用于支付成功后更新订单状态
     */
    void updateOrderStatusAfterPay(Long orderId);


    /*
    * 获取服务中订单统计数据
    * */
    Map<String, Object> getProviderStats();

    /**
     * ✅ 新增：更新订单信息（用于评价联动等场景）
     */
    void updateOrder(Order order);

    /**
     * ✅ 新增：根据条件查询订单列表（供其他 Service 跨表搜索使用）
     */
    List<Order> list(LambdaQueryWrapper<Order> wrapper);
}

