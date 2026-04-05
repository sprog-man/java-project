package com.petcompany.platform.modules.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 订单创建请求
 */
@Data
public class OrderCreateRequest {

    /**
     * 宠物ID
     */
    @NotNull(message = "宠物ID不能为空")
    private Long petId;

    /**
     * 服务类型ID
     */
    @NotNull(message = "服务类型ID不能为空")
    private Long serviceTypeId;

    /**
     * 服务开始时间
     */
    @NotNull(message = "服务开始时间不能为空")
    private LocalDateTime serviceStartTime;

    /**
     * 服务结束时间
     */
    @NotNull(message = "服务结束时间不能为空")
    private LocalDateTime serviceEndTime;

    /**
     * 服务地址
     */
    @NotBlank(message = "服务地址不能为空")
    private String address;

}
