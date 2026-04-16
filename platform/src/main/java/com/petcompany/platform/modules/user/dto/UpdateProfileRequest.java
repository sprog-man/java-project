package com.petcompany.platform.modules.user.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 用户资料更新请求 DTO
 * 原理：通过白名单机制，只暴露允许前端修改的字段，防止 Mass Assignment 攻击
 */
@Data
public class UpdateProfileRequest {

    /**
     * 昵称：限制长度，防止数据库溢出或页面布局错乱
     */
    @Size(max = 20, message = "昵称长度不能超过20个字符")
    private String nickname;

    /**
     * 邮箱：使用正则校验格式
     */
    @Email(message = "邮箱格式不正确")
    private String email;

    /**
     * 头像 URL
     */
    private String avatar;

    // 注意：这里故意不包含 id, password, role, phone 等敏感字段

}
