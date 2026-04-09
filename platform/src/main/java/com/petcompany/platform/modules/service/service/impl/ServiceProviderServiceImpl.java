package com.petcompany.platform.modules.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.petcompany.platform.common.exception.BusinessException;
import com.petcompany.platform.modules.order.dto.OrderResponse;
import com.petcompany.platform.modules.order.entity.Order;
import com.petcompany.platform.modules.order.service.OrderService;
import com.petcompany.platform.modules.service.dto.ServiceProviderApplyRequest;
import com.petcompany.platform.modules.service.entity.ServiceProvider;
import com.petcompany.platform.modules.service.service.ServiceProviderService;
import com.petcompany.platform.modules.service.mapper.ServiceProviderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 服务提供者服务实现类
 */
@Service
@Slf4j
public class ServiceProviderServiceImpl implements ServiceProviderService {

    @Resource
    private ServiceProviderMapper serviceProviderMapper;

    @Resource
    private OrderService orderService;

    @Override
    public ServiceProvider applyServiceProvider(Long userId, ServiceProviderApplyRequest request) {
        // 检查是否已经申请过
        LambdaQueryWrapper<ServiceProvider> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ServiceProvider::getUserId, userId);
        ServiceProvider existing = serviceProviderMapper.selectOne(wrapper);
        if (existing != null) {
            throw new BusinessException("您已经申请过服务提供者");
        }

        // 创建服务提供者
        ServiceProvider serviceProvider = new ServiceProvider();
        serviceProvider.setUserId(userId);
        serviceProvider.setRealName(request.getRealName());
        serviceProvider.setIdCard(request.getIdCard());
        serviceProvider.setExperience(request.getExperience());
        serviceProvider.setRating(0.0);
        serviceProvider.setStatus(0); // 待审核
        serviceProvider.setCreateTime(LocalDateTime.now());
        serviceProvider.setUpdateTime(LocalDateTime.now());
        serviceProvider.setDeleted(0);

        // 保存到数据库
        serviceProviderMapper.insert(serviceProvider);
        log.info("用户{}申请成为服务提供者", userId);

        return serviceProvider;
    }

    @Override
    public ServiceProvider getServiceProviderByUserId(Long userId) {
        LambdaQueryWrapper<ServiceProvider> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ServiceProvider::getUserId, userId);
        return serviceProviderMapper.selectOne(wrapper);
    }

    @Override
    public List<OrderResponse> getPendingOrderList() {
        // 获取待接单的订单列表
        return orderService.getPendingOrderList();
    }

    @Override
    public void acceptOrder(Long providerId, Long orderId) {
        // 接受订单
        orderService.acceptOrder(orderId);
    }

    @Override
    public void startService(Long providerId, Long orderId) {
        // 开始服务
        orderService.startOrder(orderId);
    }

    @Override
    public void completeService(Long providerId, Long orderId) {
        // 完成服务
        orderService.completeOrder(orderId);
    }

    @Override
    public List<OrderResponse> getProviderOrderList(Long providerId) {
        // 获取服务提供者的订单列表
        return orderService.getProviderOrderList();
    }

}
