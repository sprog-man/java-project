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
     * 服务类型名称
     */
    private String serviceTypeName;

    /**
     * 服务提供者名称
     */
    private String providerName;

    /**
     * 服务开始时间
     */
    private LocalDateTime serviceStartTime;

    /**
     * 服务结束时间
     */
    private LocalDateTime serviceEndTime;

    /**
     * 服务地址
     */
    private String address;

    /**
     * 订单价格
     */
    private Double price;

    /**
     * 订单状态
     */
    private Integer status;

    /**
     * 订单状态名称
     */
    private String statusName;

    /**
     * 支付状态
     */
    private Integer payStatus;

    /**
     * 支付状态名称
     */
    private String payStatusName;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}
