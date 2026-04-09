package com.petcompany.platform.modules.user.service;

import com.petcompany.platform.modules.user.dto.AdminLoginRequest;
import com.petcompany.platform.modules.user.dto.AdminLoginResponse;
import com.petcompany.platform.modules.user.dto.LoginRequest;
import com.petcompany.platform.modules.user.dto.LoginResponse;
import com.petcompany.platform.modules.user.dto.RegisterRequest;
import com.petcompany.platform.modules.user.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

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
     * 管理员登录
     */
    AdminLoginResponse adminLogin(AdminLoginRequest request);

    /**
     * 根据ID获取用户信息
     */
    User getUserById(Long id);

    /**
     * 根据手机号获取用户信息
     */
    User getUserByPhone(String phone);

    /**
     * 根据用户名获取用户信息
     */
    User getUserByUsername(String username);

    /**
     * 更新用户信息
     */
    void updateUser(User user);

    /**
     * 修改密码
     */
    void changePassword(Long userId, String oldPassword, String newPassword);

    /**
     * 上传头像
     */
    String uploadAvatar(Long userId, MultipartFile file);
    /**
     * 获取当前用户信息
     */
    Map<String, Object> getCurrentUserInfo();
}
