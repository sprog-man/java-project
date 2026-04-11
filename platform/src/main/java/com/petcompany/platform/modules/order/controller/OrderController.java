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
     * ✅ 1. 获取当前用户的订单列表 (原 /orderInfo)
     * GET /order
     */
    @GetMapping
    public Result<List<OrderResponse>> getOrderList() {
        // TODO: 后续从 SecurityContext 获取真实 userId，目前暂时查所有或硬编码
        Long currentUserId = 1L;
        log.info("接收到获取用户 {} 的订单列表请求", currentUserId);
        List<OrderResponse> orders = orderService.getOrderListByUserId(currentUserId);
        return Result.success("获取订单列表成功", orders);
    }

    /**
     * ✅ 2. 获取订单详情
     * GET /order/{id}
     */
    @GetMapping("/{id}")
    public Result<OrderResponse> getOrderDetail(@PathVariable Long id) {
        log.info("接收到获取订单详情请求: id={}", id);
        OrderResponse order = orderService.getOrderDetail(id);
        return Result.success("获取订单详情成功", order);
    }


    /**
     * ✅ 3. 创建订单
     * POST /order
     */
    @PostMapping
    public Result<OrderResponse> createOrder(@Valid @RequestBody OrderCreateRequest request) {
        log.info("接收到创建订单请求: serviceType={}, petId={}", request.getServiceType(), request.getPetId());
        OrderResponse order = orderService.createOrder(request);
        log.info("创建订单成功: orderId={}", order.getId());
        return Result.success("创建订单成功", order);
    }

    /**
     * ✅ 4. 取消订单
     * PUT /order/{id}/cancel
     */
    @PutMapping("/{id}/cancel")
    public Result<OrderResponse> cancelOrder(@PathVariable Long id) {
        log.info("接收到取消订单请求: id={}", id);
        OrderResponse order = orderService.cancelOrder(id);
        log.info("取消订单成功: orderId={}", order.getId());
        return Result.success("取消订单成功", order);
    }

    /**
     * ✅ 5. 接受订单 (服务者端)
     * PUT /order/{id}/accept
     */
    @PutMapping("/{id}/accept")
    public Result<OrderResponse> acceptOrder(@PathVariable Long id) {
        log.info("接收到接受订单请求: id={}", id);
        OrderResponse order = orderService.acceptOrder(id);
        return Result.success("接受订单成功", order);
    }

    /**
     * ✅ 6. 开始服务
     * PUT /order/{id}/start
     */
    @PutMapping("/{id}/start")
    public Result<OrderResponse> startOrder(@PathVariable Long id) {
        log.info("接收到开始服务请求: id={}", id);
        OrderResponse order = orderService.startOrder(id);
        return Result.success("开始服务成功", order);
    }

    /**
     * ✅ 7. 完成服务
     * PUT /order/{id}/complete
     */
    @PutMapping("/{id}/complete")
    public Result<OrderResponse> completeOrder(@PathVariable Long id) {
        log.info("接收到完成服务请求: id={}", id);
        OrderResponse order = orderService.completeOrder(id);
        return Result.success("完成服务成功", order);
    }

    /**
     * ✅ 8. 获取服务提供者订单列表
     * GET /order/provider
     */
    @GetMapping("/provider")
    public Result<List<OrderResponse>> getProviderOrderList() {
        log.info("接收到获取服务提供者订单列表请求");
        List<OrderResponse> orders = orderService.getProviderOrderList();
        return Result.success("获取服务提供者订单列表成功", orders);
    }

    /**
     * ✅ 9. 获取待接单订单列表
     * GET /order/pending
     */
    @GetMapping("/pending")
    public Result<List<OrderResponse>> getPendingOrderList() {
        log.info("接收到获取待接单订单列表请求");
        List<OrderResponse> orders = orderService.getPendingOrderList();
        return Result.success("获取待接单订单列表成功", orders);
    }

}
