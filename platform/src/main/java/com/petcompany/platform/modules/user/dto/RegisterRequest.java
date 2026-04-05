package com.petcompany.platform.modules.user.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * 用户注册请求DTO
 */
@Data
public class RegisterRequest {

    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d@$!%*?&]{6,20}$", message = "密码长度6-20位，包含字母和数字")
    private String password;

    /**
     * 昵称
     */
    @NotBlank(message = "昵称不能为空")
    private String nickname;

}
