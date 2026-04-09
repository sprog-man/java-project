package com.petcompany.platform.modules.order.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 订单响应DTO
 */
@Data
public class OrderResponse {

    /**
     * 订单ID
     */
    private Long id;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 宠物名称
     */
    private String petName;

    /**
     * 服务名称
     */
    private String serviceName;

    /**
     * 服务提供者名称
     */
    private String providerName;

    /**
     * 服务时间
     */
    private String serviceTime;

    /**
     * 服务地址
     */
    private String address;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 备注信息
     */
    private String notes;

    /**
     * 订单价格
     */
    private Integer price;

    /**
     * 订单状态
     */
    private String status;

    /**
     * 订单状态文本
     */
    private String statusText;

    /**
     * 支付状态文本
     */
    private String paymentStatusText;

    /**
     * 支付时间
     */
    private LocalDateTime paymentTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 是否已评价
     */
    private boolean reviewed;

}
