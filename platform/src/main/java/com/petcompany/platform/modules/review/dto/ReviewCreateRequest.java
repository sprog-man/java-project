package com.petcompany.platform.modules.review.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 评价创建请求
 */
@Data
public class ReviewCreateRequest {

    /**
     * 订单ID
     */
    @NotNull(message = "订单ID不能为空")
    private Long orderId;

    /**
     * 评分：1-5星
     */
    @NotNull(message = "评分不能为空")
    @Min(value = 1, message = "评分必须在1-5星之间")
    @Max(value = 5, message = "评分必须在1-5星之间")
    private Integer score;

    /**
     * 评价内容
     */
    @NotBlank(message = "评价内容不能为空")
    private String content;

    /**
     * 评价图片（多个图片URL用逗号分隔）
     */
    private String images;

}
