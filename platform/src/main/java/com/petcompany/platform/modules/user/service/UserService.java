package com.petcompany.platform.modules.user.service;

import com.petcompany.platform.modules.user.dto.LoginRequest;
import com.petcompany.platform.modules.user.dto.LoginResponse;
import com.petcompany.platform.modules.user.dto.RegisterRequest;
import com.petcompany.platform.modules.user.entity.User;

/**
 * 用户服务接口
 */
public interface UserService {

    /**
     * 用户注册
     */
    void register(RegisterRequest request);

    /**
     * 用户登录
     */
    LoginResponse login(LoginRequest request);

    /**
     * 根据ID获取用户信息
     */
    User getUserById(Long id);

    /**
     * 根据手机号获取用户信息
     */
    User getUserByPhone(String phone);

    /**
     * 更新用户信息
     */
    void updateUser(User user);

    /**
     * 修改密码
     */
    void changePassword(Long userId, String oldPassword, String newPassword);

}
