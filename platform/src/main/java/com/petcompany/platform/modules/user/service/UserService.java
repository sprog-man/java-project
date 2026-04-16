package com.petcompany.platform.modules.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.petcompany.platform.modules.user.dto.*;
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
    void updateUser(Long userId, UpdateProfileRequest request);

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
    /**
     * 获取管理员统计信息
     */
    AdminStatsResponse getAdminStats();

    /**
     * 分页获取用户列表
     */
    // UserService.java
    Page<User> getUserList(int pageNum, int pageSize, String keyword, Integer userType, Integer status, Integer verified, Integer role);

    /**
     * 管理员更新用户状态
     */
    void updateUserStatusByAdmin(Long userId, Integer status, Integer verified, Integer role);

    /**
     * 管理员逻辑删除用户
     */
    void deleteUserByAdmin(Long userId);

    /**
     * ✅ 新增：根据条件查询用户列表（供其他 Service 跨表搜索使用）
     */
    java.util.List<User> list(com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<User> wrapper);

}
