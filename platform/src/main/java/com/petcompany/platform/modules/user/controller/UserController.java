package com.petcompany.platform.modules.user.controller;

import com.petcompany.platform.common.exception.BusinessException;
import com.petcompany.platform.common.result.Result;
import com.petcompany.platform.common.result.ResultCode;
import com.petcompany.platform.modules.user.dto.AdminLoginRequest;
import com.petcompany.platform.modules.user.dto.AdminLoginResponse;
import com.petcompany.platform.modules.user.dto.LoginRequest;
import com.petcompany.platform.modules.user.dto.LoginResponse;
import com.petcompany.platform.modules.user.dto.RegisterRequest;
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
    public Result<?> updateUser(@Valid @RequestBody User user) {
        log.info("接收到更新用户信息请求: userId={}, username={}", user.getId(), user.getUsername());
         userService.updateUser(user);
        log.info("用户信息更新成功: userId={}, username={}", user.getId(), user.getUsername());
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
}
