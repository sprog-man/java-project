package com.petcompany.platform.modules.payment.service;

import com.petcompany.platform.modules.payment.dto.PaymentResponse;
import com.petcompany.platform.modules.payment.entity.PaymentRecord;

import java.util.List;

/**
 * 支付服务接口
 */
public interface PaymentService {

    /**
     * 创建支付
     */
    PaymentRecord createPayment(Long userId, Long orderId, Integer payType);

    /**
     * 处理支付回调
     */
    void handlePaymentCallback(String payOrderNo, String tradeNo, Integer status);

    /**
     * 获取支付记录
     */
    PaymentRecord getPaymentById(Long id);

    /**
     * 根据订单ID获取支付记录
     */
    PaymentRecord getPaymentByOrderId(Long orderId);

    /**
     * 获取用户支付记录列表
     */
    List<PaymentResponse> getUserPaymentList(Long userId);

}
