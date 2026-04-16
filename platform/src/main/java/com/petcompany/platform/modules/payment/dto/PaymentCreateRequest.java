package com.petcompany.platform.modules.payment.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 支付创建请求
 */
@Data
public class PaymentCreateRequest {

    /**
     * 订单ID
     */
    @NotNull(message = "订单ID不能为空")
    private Long orderId;

    /**
     * 支付方式：1-微信支付，2-支付宝
     */
    @NotNull(message = "支付方式不能为空")
    private Integer payType;

}
