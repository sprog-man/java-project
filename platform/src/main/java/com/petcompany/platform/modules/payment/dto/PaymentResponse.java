package com.petcompany.platform.modules.payment.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 支付响应
 */
@Data
public class PaymentResponse {

    /**
     * 主键
     */
    private Long id;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 支付金额
     */
    private BigDecimal amount;

    /**
     * 支付状态：0-待支付，1-支付成功，2-支付失败
     */
    private Integer status;

    /**
     * 支付方式：1-微信支付，2-支付宝
     */
    private String paymentMethod;

    /**
     * 支付单号
     */
    private String payOrderNo;

    /**
     * 交易单号
     */
    private String tradeNo;

    /**
     * 支付时间
     */
    private LocalDateTime payTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}
