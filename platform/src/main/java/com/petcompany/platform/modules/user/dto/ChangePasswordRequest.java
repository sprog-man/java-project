package com.petcompany.platform.modules.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 修改密码请求 DTO
 * 原理：通过白名单机制，确保后端只接收必要的密码字段，防止参数篡改
 */
@Data
public class ChangePasswordRequest {
    /**
     * 原密码：必须提供，用于验证用户身份
     */
    @NotBlank(message = "原密码不能为空")
    private String oldPassword;

    /**
     * 新密码：必须符合安全长度要求
     */
    @NotBlank(message = "新密码不能为空")
    @Size(min = 6, message = "新密码长度必须在 6 到 20 个字符之间")
    private String newPassword;
}
