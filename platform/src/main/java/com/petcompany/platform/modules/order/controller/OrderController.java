package com.petcompany.platform.modules.order.controller;

import com.petcompany.platform.common.result.Result;
import com.petcompany.platform.modules.order.dto.OrderCreateRequest;
import com.petcompany.platform.modules.order.dto.OrderResponse;
import com.petcompany.platform.modules.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import java.util.List;

/**
 * 订单控制器
 */
@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {

    @Resource
    private OrderService orderService;

    /**
     * 获取订单列表
     */
    @GetMapping("/orderInfo")
    public Result<List<OrderResponse>> getOrderList() {
        log.info("接收到获取订单列表请求");
        List<OrderResponse> orders = orderService.getOrderList();
        return Result.success("获取订单列表成功", orders);
    }

    /**
     * 获取订单详情
     */
    @GetMapping("/{id}")
    public Result<OrderResponse> getOrderDetail(@PathVariable Long id) {
        log.info("接收到获取订单详情请求: id={}", id);
        OrderResponse order = orderService.getOrderDetail(id);
        return Result.success("获取订单详情成功", order);
    }

    /**
     * 创建订单
     */
    @PostMapping("/create")
    public Result<OrderResponse> createOrder(@Valid @RequestBody OrderCreateRequest request) {
        log.info("接收到创建订单请求: serviceType={}, serviceTime={}", request.getServiceType(), request.getServiceTime());
        OrderResponse order = orderService.createOrder(request);
        log.info("创建订单成功: orderId={}", order.getId());
        return Result.success("创建订单成功", order);
    }

    /**
     * 取消订单
     */
    @PutMapping("/{id}/cancel")
    public Result<OrderResponse> cancelOrder(@PathVariable Long id) {
        log.info("接收到取消订单请求: id={}", id);
        OrderResponse order = orderService.cancelOrder(id);
        log.info("取消订单成功: orderId={}", order.getId());
        return Result.success("取消订单成功", order);
    }

    /**
     * 接受订单
     */
    @PutMapping("/{id}/accept")
    public Result<OrderResponse> acceptOrder(@PathVariable Long id) {
        log.info("接收到接受订单请求: id={}", id);
        OrderResponse order = orderService.acceptOrder(id);
        log.info("接受订单成功: orderId={}", order.getId());
        return Result.success("接受订单成功", order);
    }

    /**
     * 开始服务
     */
    @PutMapping("/{id}/start")
    public Result<OrderResponse> startOrder(@PathVariable Long id) {
        log.info("接收到开始服务请求: id={}", id);
        OrderResponse order = orderService.startOrder(id);
        log.info("开始服务成功: orderId={}", order.getId());
        return Result.success("开始服务成功", order);
    }

    /**
     * 完成服务
     */
    @PutMapping("/{id}/complete")
    public Result<OrderResponse> completeOrder(@PathVariable Long id) {
        log.info("接收到完成服务请求: id={}", id);
        OrderResponse order = orderService.completeOrder(id);
        log.info("完成服务成功: orderId={}", order.getId());
        return Result.success("完成服务成功", order);
    }

    /**
     * 获取服务提供者订单列表
     */
    @GetMapping("/provider")
    public Result<List<OrderResponse>> getProviderOrderList() {
        log.info("接收到获取服务提供者订单列表请求");
        List<OrderResponse> orders = orderService.getProviderOrderList();
        return Result.success("获取服务提供者订单列表成功", orders);
    }

    /**
     * 获取待接单订单列表
     */
    @GetMapping("/pending")
    public Result<List<OrderResponse>> getPendingOrderList() {
        log.info("接收到获取待接单订单列表请求");
        List<OrderResponse> orders = orderService.getPendingOrderList();
        return Result.success("获取待接单订单列表成功", orders);
    }

}
