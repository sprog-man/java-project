package com.petcompany.platform.modules.user.dto;

import lombok.Data;

/**
 * 用户登录响应DTO
 */
@Data
public class LoginResponse {

    /**
     * 访问令牌
     */
    private String token;

    /**
     * 过期时间（秒）
     */
    private Long expire;

    /**
     * 用户信息
     */
    private UserInfo userInfo;

    /**
     * 用户信息内部类
     */
    @Data
    public static class UserInfo {

        /**
         * 用户ID
         */
        private Long id;

        /**
         * 手机号
         */
        private String phone;

        /**
         * 用户名
         */
        private String username;

        /**
         * 头像
         */
        private String avatar;

        /**
         * 用户类型
         */
        private Integer userType;

        /**
         * 认证状态
         */
        private Integer verified;

        /**
         * 邮箱
         */
        private String email;

    }

}
