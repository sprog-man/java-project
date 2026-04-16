package com.petcompany.platform.modules.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

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
    private Long serviceType;

    /**
     * 服务日期
     */
    @NotBlank(message = "服务日期不能为空")
    private String serviceDate;

    /**
     * 服务开始时间
     */
    @NotBlank(message = "服务开始时间不能为空")
    private String serviceStartTime;

    /**
     * 服务结束时间
     */
    @NotBlank(message = "服务结束时间不能为空")
    private String serviceEndTime;

    /**
     * 服务地址
     */
    @NotBlank(message = "服务地址不能为空")
    private String address;

    /**
     * 联系电话
     */
    @NotBlank(message = "联系电话不能为空")
    private String phone;

    /**
     * 备注信息
     */
    private String notes;

    /**
     * 服务时间（格式：YYYY-MM-DD HH:MM-HH:MM）
     */
    private String serviceTime;

    /**
     * 订单价格
     */
    @NotNull(message = "订单价格不能为空")
    private BigDecimal price; // ✅ 修改为 BigDecimal 以支持小数金额

}
