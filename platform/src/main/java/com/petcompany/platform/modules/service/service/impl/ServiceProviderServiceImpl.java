package com.petcompany.platform.modules.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.petcompany.platform.common.exception.BusinessException;
import com.petcompany.platform.modules.order.dto.OrderResponse;
import com.petcompany.platform.modules.order.service.OrderService;
import com.petcompany.platform.modules.service.dto.ServiceProviderApplyRequest;
import com.petcompany.platform.modules.service.entity.ServiceProvider;
import com.petcompany.platform.modules.service.service.ServiceProviderService;
import com.petcompany.platform.modules.service.mapper.ServiceProviderMapper;
import com.petcompany.platform.modules.user.entity.User;
import com.petcompany.platform.modules.user.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import org.springframework.transaction.annotation.Transactional;

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

    @Resource
    private UserMapper userMapper;// ✅ 注入 UserMapper 用于操作用户表


    @Override
    public ServiceProvider applyServiceProvider(Long userId, ServiceProviderApplyRequest request) {
        // ✅ 1. 增加调试日志，确认 userId 是否成功传入
        log.info("开始处理申请: userId={}, realName={}", userId, request.getRealName());

        if (userId == null) {
            throw new BusinessException("用户未登录或会话已过期，请重新登录");
        }

        // 检查是否已经申请过
        LambdaQueryWrapper<ServiceProvider> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ServiceProvider::getUserId, userId);
        ServiceProvider existing = serviceProviderMapper.selectOne(wrapper);

        if (existing != null) {
            // ✅ 建议：如果已经申请过，可以更新他的信息，而不是直接报错
            // 或者直接抛出异常提示用户“您已提交申请，请耐心等待”
            throw new BusinessException("您已经提交过申请，请勿重复提交。如需修改请联系管理员。");
        }

        // 创建服务提供者
        ServiceProvider serviceProvider = new ServiceProvider();
        serviceProvider.setUserId(userId);
        serviceProvider.setRealName(request.getRealName());
        serviceProvider.setIdCard(request.getIdCard());
        serviceProvider.setExperience(request.getExperience()); // ✅ 确保这一行存在
        serviceProvider.setLatitude(request.getLatitude());
        serviceProvider.setLongitude(request.getLongitude());
        serviceProvider.setRating(5.0); // ✅ 初始评分建议设为 5.0
        serviceProvider.setVerifyStatus(0); // ✅ 确保这里是 0 (待审核)
        serviceProvider.setCreateTime(LocalDateTime.now());
        serviceProvider.setUpdateTime(LocalDateTime.now());
        serviceProvider.setDeleted(0);

        // 保存到数据库
        try {
            serviceProviderMapper.insert(serviceProvider);
            log.info("用户{}申请成为服务提供者成功", userId);
        } catch (Exception e) {
            log.error("数据库插入失败", e);
            throw new BusinessException("申请保存失败: " + e.getMessage());
        }
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

    @Override
    public Long countCompletedOrders(Long providerId) {
        // 1.先根据userId找到providerId
        ServiceProvider provider = getServiceProviderByUserId(providerId);
        if (provider == null) {
            return 0L;
        }
        // 2. 调用 OrderService 进行统计
        // 假设 OrderService 中有根据 providerId 和 status 计数的方法
        return orderService.countByProviderIdAndStatus(provider.getId(), 5); // 5-已完成

    }


    /**
     * 管理员审核服务者申请
     */
    @Override
    @Transactional(rollbackFor = Exception.class) // ✅ 开启事务，确保两张表要么都成功，要么都失败
    public void auditApplication(Long providerId, Integer status) {
        // 1. 获取服务者信息
        ServiceProvider provider = serviceProviderMapper.selectById(providerId);
        if (provider == null) {
            throw new BusinessException("申请记录不存在");
        }

        // 2. 更新 service_provider 表的 verify_status
        provider.setVerifyStatus(status);
        provider.setUpdateTime(LocalDateTime.now());
        serviceProviderMapper.updateById(provider);

        // 3. 联动更新 sys_user 表的 verified 字段
        // 只有当审核通过（status == 1）时，才将用户的认证状态设为 1
        if (status == 1) {
            User user = userMapper.selectById(provider.getUserId());
            if (user != null) {
                user.setVerified(1);
                user.setUpdateTime(LocalDateTime.now());
                userMapper.updateById(user);
                log.info("审核通过，已同步更新用户 {} 的认证状态为已认证", user.getId());
            }
        } else {
            // 如果审核拒绝，可以选择保持用户 verified 为 0，或者根据需求重置
            log.info("审核拒绝，用户 {} 保持未认证状态", provider.getUserId());
        }

        log.info("管理员完成审核: providerId={}, status={}", providerId, status);
    }

    /**
     * 管理员获取待审核的服务者申请列表
     */
    @Override
    public List<ServiceProvider> getPendingApplications() {
        LambdaQueryWrapper<ServiceProvider> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ServiceProvider::getVerifyStatus, 0); // 0-待审核
        wrapper.eq(ServiceProvider::getDeleted, 0);
        wrapper.orderByDesc(ServiceProvider::getCreateTime);
        return serviceProviderMapper.selectList(wrapper);
    }
}
