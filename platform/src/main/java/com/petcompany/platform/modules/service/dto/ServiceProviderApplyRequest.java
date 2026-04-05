package com.petcompany.platform.modules.service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 服务提供者申请请求
 */
@Data
public class ServiceProviderApplyRequest {

    /**
     * 真实姓名
     */
    @NotBlank(message = "真实姓名不能为空")
    private String realName;

    /**
     * 身份证号
     */
    @NotBlank(message = "身份证号不能为空")
    private String idCard;

    /**
     * 服务经验
     */
    @NotBlank(message = "服务经验不能为空")
    private String experience;

}
