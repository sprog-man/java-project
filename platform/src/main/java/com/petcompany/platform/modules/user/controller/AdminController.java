package com.petcompany.platform.modules.user.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.petcompany.platform.common.result.Result;
import com.petcompany.platform.modules.user.dto.AdminStatsResponse;
import com.petcompany.platform.modules.user.entity.User;
import com.petcompany.platform.modules.user.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin")
@Slf4j
public class AdminController {

    @Resource
    private UserService userService;

    /**
     * 管理员后台分页获取用户列表
     * 分页获取用户列表
     * RESTful: GET /admin/users?page=1&size=10
     */
    @GetMapping("/users")
    public Result<Page<User>> getUserList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer userType, // ✅ 接收用户类型筛选
            @RequestParam(required = false) Integer status,   // ✅ 接收状态筛选
            @RequestParam(required = false) Integer verified, // ✅ 接收认证状态筛选
            @RequestParam(required = false) Integer role) {   // ✅ 接收角色筛选

        log.info("管理员查询用户: page={}, keyword={}, status={}", page, keyword, status);

        // 将参数传递给 Service 层进行动态 SQL 拼接
        Page<User> result = userService.getUserList(page, size, keyword, userType, status, verified, role);
        return Result.success(result);
    }

    /**
     * 管理员获取后台统计数据
     */
    @GetMapping("/stats")
    public Result<AdminStatsResponse> getAdminStats() {
        AdminStatsResponse stats = userService.getAdminStats();
        return Result.success(stats);
    }

    /**
     * 管理员更新用户状态
     */
    @PutMapping("/users/{id}/status")
    public  Result<?> updateUserStatus(@PathVariable Long id, @RequestBody Map<String, Integer> updates){
        log.info("管理员更新用户状态: id={}, updates={}", id, updates);
        userService.updateUserStatusByAdmin(id, updates.get("status"), updates.get("verified"), updates.get("role"));
        return Result.success("更新成功");
    }

    /**
     * 管理员删除用户（逻辑删除）
     * RESTful: DELETE /admin/users/{id}
     */
    @DeleteMapping("/users/{id}")
    public Result<?> deleteUser(@PathVariable Long id) {
        log.info("管理员执行删除用户操作: userId={}", id);
        userService.deleteUserByAdmin(id);
        return Result.success("删除成功");
    }
}
