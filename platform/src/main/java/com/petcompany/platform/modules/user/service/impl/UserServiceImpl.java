package com.petcompany.platform.modules.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.petcompany.platform.common.exception.BusinessException;
import com.petcompany.platform.infrastructure.security.JwtService;
import com.petcompany.platform.modules.user.dto.LoginRequest;
import com.petcompany.platform.modules.user.dto.LoginResponse;
import com.petcompany.platform.modules.user.dto.RegisterRequest;
import com.petcompany.platform.modules.user.entity.User;
import com.petcompany.platform.modules.user.service.UserService;
import com.petcompany.platform.modules.user.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户服务实现类
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private JwtService jwtService;

    @Value("${jwt.expire}")
    private Long expire;

    @Override
    public void register(RegisterRequest request) {
        // 检查手机号是否已存在
        User existingUser = getUserByPhone(request.getPhone());
        if (existingUser != null) {
            throw new BusinessException("手机号已注册");
        }

        // 创建新用户
        User user = new User();
        user.setPhone(request.getPhone());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setNickname(request.getNickname());
        user.setUserType(0); // 默认宠物主
        user.setStatus(0); // 正常
        user.setVerified(0); // 未认证
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        user.setDeleted(0); // 未删除

        userMapper.insert(user);
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        // 根据手机号查找用户
        User user = getUserByPhone(request.getPhone());
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        // 检查密码
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException("密码错误");
        }

        // 检查用户状态
        if (user.getStatus() == 1) {
            throw new BusinessException("账号已禁用");
        }

        // 生成JWT令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("userType", user.getUserType());
        String token = jwtService.generateToken(claims);

        // 构建响应
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setExpire(expire);

        LoginResponse.UserInfo userInfo = new LoginResponse.UserInfo();
        userInfo.setId(user.getId());
        userInfo.setPhone(user.getPhone());
        userInfo.setNickname(user.getNickname());
        userInfo.setAvatar(user.getAvatar());
        userInfo.setUserType(user.getUserType());
        userInfo.setVerified(user.getVerified());

        response.setUserInfo(userInfo);

        return response;
    }

    @Override
    public User getUserById(Long id) {
        return userMapper.selectById(id);
    }

    @Override
    public User getUserByPhone(String phone) {
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(User::getPhone, phone);
        return userMapper.selectOne(wrapper);
    }

    @Override
    public void updateUser(User user) {
        user.setUpdateTime(LocalDateTime.now());
        userMapper.updateById(user);
    }

    @Override
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        // 验证旧密码
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BusinessException("旧密码错误");
        }

        // 更新密码
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdateTime(LocalDateTime.now());
        userMapper.updateById(user);
    }

}
