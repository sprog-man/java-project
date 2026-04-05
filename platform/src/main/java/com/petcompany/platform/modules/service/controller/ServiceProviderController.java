package com.petcompany.platform.modules.service.controller;

import com.petcompany.platform.common.result.Result;
import com.petcompany.platform.infrastructure.security.annotation.RequiresPermission;
import com.petcompany.platform.modules.order.dto.OrderResponse;
import com.petcompany.platform.modules.service.dto.ServiceProviderApplyRequest;
import com.petcompany.platform.modules.service.entity.ServiceProvider;
import com.petcompany.platform.modules.service.service.ServiceProviderService;
import com.petcompany.platform.infrastructure.security.UserContext;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import java.util.List;

/**
 * 服务提供者控制器
 */
@RestController
@RequestMapping("/service-provider")
@Slf4j
public class ServiceProviderController {

    @Resource
    private ServiceProviderService serviceProviderService;

    /**
     * 申请成为服务提供者
     */
    @PostMapping("/apply")
    public Result<ServiceProvider> applyServiceProvider(@Valid @RequestBody ServiceProviderApplyRequest request) {
        Long userId = UserContext.getCurrentUserId();
        log.info("用户{}申请成为服务提供者", userId);

        ServiceProvider serviceProvider = serviceProviderService.applyServiceProvider(userId, request);
        return Result.success(serviceProvider);
    }

    /**
     * 获取服务提供者信息
     */
    @GetMapping("/info")
    @RequiresPermission(userTypes = {1})
    public Result<ServiceProvider> getServiceProviderInfo() {
        Long userId = UserContext.getCurrentUserId();
        log.info("获取用户{}的服务提供者信息", userId);

        ServiceProvider serviceProvider = serviceProviderService.getServiceProviderByUserId(userId);
        if (serviceProvider == null) {
            return Result.fail(com.petcompany.platform.common.result.ResultCode.NOT_FOUND);
        }
        return Result.success(serviceProvider);
    }

    /**
     * 获取待接单的订单列表
     */
    @GetMapping("/pending-orders")
    @RequiresPermission(userTypes = {1})
    public Result<List<OrderResponse>> getPendingOrderList() {
        Long userId = UserContext.getCurrentUserId();
        log.info("服务提供者{}获取待接单订单列表", userId);

        List<OrderResponse> list = serviceProviderService.getPendingOrderList();
        return Result.success(list);
    }

    /**
     * 接受订单
     */
    @PostMapping("/accept-order/{orderId}")
    @RequiresPermission(userTypes = {1})
    public Result<String> acceptOrder(@PathVariable Long orderId) {
        Long userId = UserContext.getCurrentUserId();
        log.info("服务提供者{}接受订单{}", userId, orderId);

        serviceProviderService.acceptOrder(userId, orderId);
        return Result.success("接受订单成功");
    }

    /**
     * 开始服务
     */
    @PostMapping("/start-service/{orderId}")
    @RequiresPermission(userTypes = {1})
    public Result<String> startService(@PathVariable Long orderId) {
        Long userId = UserContext.getCurrentUserId();
        log.info("服务提供者{}开始服务{}", userId, orderId);

        serviceProviderService.startService(userId, orderId);
        return Result.success("开始服务成功");
    }

    /**
     * 完成服务
     */
    @PostMapping("/complete-service/{orderId}")
    @RequiresPermission(userTypes = {1})
    public Result<String> completeService(@PathVariable Long orderId) {
        Long userId = UserContext.getCurrentUserId();
        log.info("服务提供者{}完成服务{}", userId, orderId);

        serviceProviderService.completeService(userId, orderId);
        return Result.success("完成服务成功");
    }

    /**
     * 获取服务提供者的订单列表
     */
    @GetMapping("/orders")
    @RequiresPermission(userTypes = {1})
    public Result<List<OrderResponse>> getProviderOrderList() {
        Long userId = UserContext.getCurrentUserId();
        log.info("获取服务提供者{}的订单列表", userId);

        List<OrderResponse> list = serviceProviderService.getProviderOrderList(userId);
        return Result.success(list);
    }

}
