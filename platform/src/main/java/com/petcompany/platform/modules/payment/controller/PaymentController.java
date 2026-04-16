package com.petcompany.platform.modules.payment.controller;

import com.petcompany.platform.common.result.Result;
import com.petcompany.platform.infrastructure.security.UserContext;
import com.petcompany.platform.modules.payment.dto.PaymentCreateRequest;
import com.petcompany.platform.modules.payment.dto.PaymentResponse;
import com.petcompany.platform.modules.payment.service.PaymentService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import java.util.List;

/**
 * 支付控制器
 */
@RestController
@RequestMapping("/payment")
@Slf4j
public class PaymentController {

    @Resource
    private PaymentService paymentService;

    /**
     * 创建支付
     */
    @PostMapping("/create")
    public Result<?> createPayment(@Valid @RequestBody PaymentCreateRequest request) {
        Long userId = UserContext.getCurrentUserId();
        log.info("用户{}创建支付，订单ID：{}", userId, request.getOrderId());

        paymentService.createPayment(userId, request.getOrderId(), request.getPayType());
        return Result.success("支付创建成功");
    }

    /**
     * 获取支付记录
     */
    @GetMapping("/{id}")
    public Result<?> getPaymentById(@PathVariable Long id) {
        log.info("获取支付记录，ID：{}", id);

        return Result.success(paymentService.getPaymentById(id));
    }

    /**
     * 根据订单ID获取支付记录
     */
    @GetMapping("/order/{orderId}")
    public Result<?> getPaymentByOrderId(@PathVariable Long orderId) {
        log.info("根据订单ID获取支付记录，订单ID：{}", orderId);

        return Result.success(paymentService.getPaymentByOrderId(orderId));
    }

    /**
     * 获取用户支付记录列表
     */
    @GetMapping("/list")
    public Result<List<PaymentResponse>> getUserPaymentList() {
        Long userId = UserContext.getCurrentUserId();
        log.info("获取用户{}的支付记录列表", userId);

        List<PaymentResponse> list = paymentService.getUserPaymentList(userId);
        return Result.success(list);
    }

    /**
     * 支付回调
     */
    @PostMapping("/callback")
    public Result<?> paymentCallback(@RequestParam String payOrderNo, @RequestParam String tradeNo, @RequestParam Integer status) {
        log.info("支付回调，支付单号：{}，交易单号：{}，状态：{}", payOrderNo, tradeNo, status);

        paymentService.handlePaymentCallback(payOrderNo, tradeNo, status);
        return Result.success("回调处理成功");
    }

    /**
     * ✅ 新增：模拟支付成功接口 (仅供开发测试使用)
     * GET /payment/mock/{orderId}
     */
    @GetMapping("/mock/{orderId}")
    public Result<?> mockPaymentSuccess(@PathVariable Long orderId) {
        log.info("开始模拟支付订单: {}", orderId);

        // 1. 获取该订单的支付记录
        var payment = paymentService.getPaymentByOrderId(orderId);
        if (payment == null) {
            // 如果还没创建支付记录，先创建一个
            Long userId = UserContext.getCurrentUserId();
            payment = paymentService.createPayment(userId, orderId, 1);
        }

        // 2. 直接调用回调逻辑，假装微信通知我们要到账了
        paymentService.handlePaymentCallback(payment.getPayOrderNo(), "MOCK_TRADE_" + System.currentTimeMillis(), 1);

        return Result.success("模拟支付成功！订单状态已更新为【待接单】");
    }

}
