package com.petcompany.platform.modules.review.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 评价响应
 */
@Data
public class ReviewResponse {

    /**
     * 主键
     */
    private Long id;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 服务提供者ID
     */
    private Long providerId;

    /**
     * 评分：1-5星
     */
    private Integer score;

    /**
     * 评价内容
     */
    private String content;

    /**
     * 评价图片（多个图片URL用逗号分隔）
     */
    private String images;

    /**
     * 回复内容
     */
    private String reply;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 服务提供者名称
     */
    private String providerName;



}
