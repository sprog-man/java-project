package com.petcompany.platform.modules.user.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.petcompany.platform.common.exception.BusinessException;
import com.petcompany.platform.common.result.Result;
import com.petcompany.platform.common.result.ResultCode;
import com.petcompany.platform.modules.user.dto.*;
import com.petcompany.platform.modules.user.entity.User;
import com.petcompany.platform.modules.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result<?> register(@Valid @RequestBody RegisterRequest request) {
        /*log是lombok自动创建的一个机制，只要你有使用@Slf4j注解，他会在你编译的时候自动注入一段创建log对象的代码，所以你在你正常代码中直接调用log对象即可*/
        log.info("接收到注册请求: phone={}, username={}", request.getPhone(), request.getUsername());
        userService.register(request);
        log.info("注册成功: phone={}", request.getPhone());
        return Result.success("注册成功");
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = userService.login(request);
        return Result.success("登录成功", response);
    }

    /**
     * 管理员登录
     */
    @PostMapping("/admin/login")
    public Result<AdminLoginResponse> adminLogin(@Valid @RequestBody AdminLoginRequest request) {
        log.info("接收到管理员登录请求: username={}", request.getUsername());
        AdminLoginResponse response = userService.adminLogin(request);
        return Result.success("登录成功", response);
    }

    /**
     * 上传头像
     */
    @PostMapping("/{id}/avatar")
    public Result<Map<String, String>> uploadAvatar(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        log.info("接收到上传头像请求: userId={}, fileName={}", id, file.getOriginalFilename());
        String avatarUrl = userService.uploadAvatar(id, file);
        log.info("头像上传成功: userId={}, avatarUrl={}", id, avatarUrl);
        return Result.success("头像上传成功", Map.of("avatarUrl", avatarUrl));
    }

    /*
    * 更新用户信息
    * */
    @PutMapping("/center/modify")
    public Result<?> updateUser(@Valid @RequestBody  UpdateProfileRequest request) {
        // 1. 从 Spring Security 上下文中获取当前登录用户的 ID
        // 这一步至关重要：它确保了操作只能针对“自己”进行，防止水平越权
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()){
            return Result.fail(ResultCode.UNAUTHORIZED);
        }
        // 从 CustomUserDetails 中获取用户 ID
        Long userId;
        Object principal = authentication.getPrincipal();
        if (principal instanceof com.petcompany.platform.infrastructure.security.CustomUserDetails) {
            userId = ((com.petcompany.platform.infrastructure.security.CustomUserDetails) principal).getUserId();
        } else {
            return Result.fail("无法获取当前用户信息");
        }
        log.info("接收到更新用户信息请求: userId={},nickname={}", userId,request.getNickname());
        // 2. 将 DTO 和 userId 传递给 Service 层处理
        userService.updateUser(userId, request);
        log.info("用户信息更新成功: userId={}", userId);
        return Result.success("用户信息更新成功");

    }
    /*前端获取用户信息
    * */
    @GetMapping("/info")
    public Result<?> getUserInfo() {
        // 从 SecurityContext 中获取当前用户
        try {
            Map<String, Object> userInfo = userService.getCurrentUserInfo();
            return Result.success(userInfo);
        } catch (BusinessException e) {
            return Result.fail(e.getMessage());
        } catch (Exception e) {
            return Result.fail(ResultCode.SYSTEM_ERROR);
        }
    }
    /*
    * 修改密码
    * */
    @PutMapping("/center/password")
    public Result<?> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        // 1. 从 Spring Security 上下文中获取当前登录用户的 ID
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Result.fail(ResultCode.UNAUTHORIZED);
        }
        // 从 CustomUserDetails 中获取用户 ID
        Long userId;
        Object principal = authentication.getPrincipal();
        if (principal instanceof com.petcompany.platform.infrastructure.security.CustomUserDetails) {
            userId = ((com.petcompany.platform.infrastructure.security.CustomUserDetails) principal).getUserId();
        } else {
            return Result.fail("无法获取当前用户信息");
        }
        log.info("接收到修改密码请求: userId={}", userId);

        // 2. 调用 Service 层处理业务逻辑
        userService.changePassword(userId, request.getOldPassword(), request.getNewPassword());
        log.info("密码修改成功: userId={}", userId);
        return Result.success("密码修改成功");

    }


}

