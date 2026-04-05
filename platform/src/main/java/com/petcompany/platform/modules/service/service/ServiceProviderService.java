package com.petcompany.platform.modules.service.service;

import com.petcompany.platform.modules.order.dto.OrderResponse;
import com.petcompany.platform.modules.service.dto.ServiceProviderApplyRequest;
import com.petcompany.platform.modules.service.entity.ServiceProvider;

import java.util.List;

/**
 * 服务提供者服务接口
 */
public interface ServiceProviderService {

    /**
     * 申请成为服务提供者
     */
    ServiceProvider applyServiceProvider(Long userId, ServiceProviderApplyRequest request);

    /**
     * 根据用户ID获取服务提供者信息
     */
    ServiceProvider getServiceProviderByUserId(Long userId);

    /**
     * 获取待接单的订单列表
     */
    List<OrderResponse> getPendingOrderList();

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
     * 获取服务提供者的订单列表
     */
    List<OrderResponse> getProviderOrderList(Long providerId);

}
