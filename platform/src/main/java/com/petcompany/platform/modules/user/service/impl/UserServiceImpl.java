package com.petcompany.platform.modules.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.petcompany.platform.common.exception.BusinessException;
import com.petcompany.platform.infrastructure.security.JwtService;
import com.petcompany.platform.modules.order.entity.Order;
import com.petcompany.platform.modules.order.mapper.OrderMapper;
import com.petcompany.platform.modules.review.entity.Review;
import com.petcompany.platform.modules.review.mapper.ReviewMapper;
import com.petcompany.platform.modules.service.entity.ServiceType;
import com.petcompany.platform.modules.service.mapper.ServiceTypeMapper;
import com.petcompany.platform.modules.user.dto.*;
import com.petcompany.platform.modules.user.entity.User;
import com.petcompany.platform.modules.user.service.UserService;
import com.petcompany.platform.modules.user.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 用户服务实现类
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private ServiceTypeMapper serviceTypeMapper;
    @Resource
    private OrderMapper orderMapper;
    @Resource
    private ReviewMapper reviewMapper;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private JwtService jwtService;

    @Value("${jwt.expire}")
    private Long expire;

    @Value("${upload.base-path}")
    private String basePath;

    @Value("${upload.avatar-path}")
    private String avatarPath;

    @Value("${upload.relative-path}")
    private String relativePath;

    @Override
    public void register(RegisterRequest request) {
        log.info("开始注册用户: phone={}, username={}, nickname={}", request.getPhone(), request.getUsername(), request.getNickname());
        // 检查手机号是否已存在
        log.info("检查手机号是否已存在: {}", request.getPhone());
        User existingUser = getUserByPhone(request.getPhone());
        if (existingUser != null) {
            log.warn("手机号已注册: {}", request.getPhone());
            throw new BusinessException("手机号已注册");
        }

        // 创建新用户
        log.info("创建新用户");
        User user = new User();
        user.setPhone(request.getPhone());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setUsername(request.getUsername());
        // 如果昵称为空，使用用户名作为昵称
        user.setNickname(request.getNickname() != null && !request.getNickname().isEmpty() ? request.getNickname() : request.getUsername());
        user.setUserType(0); // 默认宠物主
        user.setStatus(0); // 正常
        user.setVerified(0); // 未认证
        user.setRole(0); // 默认普通用户
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        user.setDeleted(0); // 未删除

        log.info("保存用户到数据库: phone={}, username={}", request.getPhone(), request.getUsername());
        int result = userMapper.insert(user);
        log.info("数据库插入结果: {}", result);
        if (result > 0) {
            log.info("用户注册成功: phone={}, username={}, userId={}", request.getPhone(), request.getUsername(), user.getId());
        } else {
            log.error("用户注册失败: phone={}, username={}", request.getPhone(), request.getUsername());
            throw new BusinessException("注册失败");
        }
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
        claims.put("role", user.getRole());
        String token = jwtService.generateToken(claims);

        // 构建响应
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setExpire(expire);

        LoginResponse.UserInfo userInfo = new LoginResponse.UserInfo();
        userInfo.setId(user.getId());
        userInfo.setPhone(user.getPhone());
        userInfo.setUsername(user.getUsername());
        userInfo.setAvatar(user.getAvatar());
        userInfo.setUserType(user.getUserType());
        userInfo.setVerified(user.getVerified());
        userInfo.setEmail(user.getEmail());

        response.setUserInfo(userInfo);
        /*return到哪里去？涉及到mvc机制，谁请求到UserController对应的路径调用该方法，就返回给谁*/
        return response;
    }

    @Override
    public AdminLoginResponse adminLogin(AdminLoginRequest request) {
        // 根据用户名查找用户
        User user = getUserByUsername(request.getUsername());
        if (user == null) {
            throw new BusinessException("管理员不存在");
        }

        // 检查是否是管理员
        if (user.getRole() != 1) {
            throw new BusinessException("不是管理员账号");
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
        claims.put("role", user.getRole());
        String token = jwtService.generateToken(claims);

        // 构建响应
        AdminLoginResponse response = new AdminLoginResponse();
        response.setAccessToken(token);

        AdminLoginResponse.AdminInfo adminInfo = new AdminLoginResponse.AdminInfo();
        adminInfo.setId(user.getId());
        adminInfo.setUsername(user.getUsername());
        adminInfo.setNickname(user.getNickname());
        adminInfo.setAvatar(user.getAvatar());

        response.setAdminInfo(adminInfo);

        return response;
    }

    @Override
    public User getUserById(Long id) {
        return userMapper.selectById(id);
    }

    @Override
    public User getUserByPhone(String phone) {
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        // 优先通过用户名查询，然后是手机号，最后是昵称
        wrapper.eq(User::getUsername, phone).or().eq(User::getPhone, phone).or().eq(User::getNickname, phone);
        return userMapper.selectOne(wrapper);
    }

    @Override
    public User getUserByUsername(String username) {
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(User::getUsername, username);
        return userMapper.selectOne(wrapper);
    }

    @Override
    public void updateUser(Long userId, UpdateProfileRequest request) {
        // 1. 根据 ID 查询数据库中的现有用户
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        // 2. 选择性更新（只更新 DTO 中不为 null 的字段）
        // 原理：如果前端没传某个字段，我们就不覆盖数据库里的旧值
        if (request.getNickname() != null){
            user.setNickname(request.getNickname());
        }
        if (request.getEmail() != null){
            user.setEmail(request.getEmail());
        }
        if (request.getAvatar() != null){
            user.setAvatar(request.getAvatar());
        }
        // 3. 执行更新
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
        userMapper.updateById(user);

    }

    @Override
    public String uploadAvatar(Long userId, MultipartFile file) {
        // 验证用户是否存在
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        // 验证文件是否为空
        if (file.isEmpty()) {
            throw new BusinessException("文件不能为空");
        }

        // 生成UUID文件名
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf('.')) : ".jpg";
        String fileName = UUID.randomUUID().toString() + extension;

        // 生成日期目录
        LocalDate now = LocalDate.now();
        String datePath = now.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

        // 构建完整的文件路径
        String fullPath = basePath + avatarPath + datePath;
        File directory = new File(fullPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // 保存文件
        File dest = new File(directory, fileName);
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            log.error("保存头像文件失败", e);
            throw new BusinessException("保存头像失败");
        }

        // 生成相对路径
        String relativeAvatarPath = relativePath + avatarPath + datePath + "/" + fileName;

        // 更新数据库
        user.setAvatar(relativeAvatarPath);
        user.setUpdateTime(LocalDateTime.now());
        userMapper.updateById(user);

        return relativeAvatarPath;
    }

    /*前端获取用户信息*/
    @Override
    public Map<String, Object> getCurrentUserInfo() {
        // 从SecurityContext中获取用户ID
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BusinessException("用户未登录");
        }

        Long userId = (Long) authentication.getPrincipal();
        User user = getUserById(userId);

        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        // 构建用户信息响应
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", user.getId());
        userInfo.put("username", user.getUsername());
        userInfo.put("phone", user.getPhone());
        userInfo.put("email", user.getEmail());
        userInfo.put("nickname", user.getNickname());
        userInfo.put("avatar", user.getAvatar());
        userInfo.put("userType", user.getUserType());
        userInfo.put("verified", user.getVerified());
        userInfo.put("role", user.getRole());

        return userInfo;
    }

    /*管理员首页获取相关信息*/
    @Override
    public AdminStatsResponse getAdminStats() {
        log.info("开始通过 Mapper 直接统计后台数据...");
        // 1. 统计用户
        long userCount = userMapper.selectCount(Wrappers.lambdaQuery(User.class).eq(User::getDeleted, 0));

        // 2. 统计评价 (直接调 Mapper)
        long reviewCount = reviewMapper.selectCount(Wrappers.lambdaQuery(Review.class).eq(Review::getDeleted, 0));

        // 3. 统计订单
        long orderCount = orderMapper.selectCount(Wrappers.lambdaQuery(Order.class).eq(Order::getDeleted, 0));

        // 4. 统计服务类型
        long serviceCount = serviceTypeMapper.selectCount(Wrappers.lambdaQuery(ServiceType.class).eq(ServiceType::getDeleted, 0));

        log.info("统计数据获取完成: 用户={}, 服务={}, 订单={}, 评价={}",
                userCount, serviceCount, orderCount, reviewCount);

        return new AdminStatsResponse(userCount, serviceCount, orderCount, reviewCount);
    }

    /*
     * 分页获取用户列表
     * */
    @Override
    public Page<User> getUserList(int pageNum, int pageSize, String keyword, Integer userType, Integer status, Integer verified, Integer role) {
        Page<User> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();

        // 1. 过滤已删除用户
        wrapper.eq(User::getDeleted, 0);

        // 2. 关键词搜索（用户名、手机号或昵称）
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(User::getUsername, keyword)
                    .or().like(User::getPhone, keyword)
                    .or().like(User::getNickname, keyword));
        }

        // ✅ 3. 动态拼接筛选条件 (修复了重复代码和变量未定义的问题)
        if (userType != null) {
            wrapper.eq(User::getUserType, userType);
        }
        if (status != null) {
            wrapper.eq(User::getStatus, status);
        }
        if (verified != null) {
            wrapper.eq(User::getVerified, verified);
        }
        if (role != null) {
            wrapper.eq(User::getRole, role);
        }

        // 4. 按创建时间倒序
        wrapper.orderByDesc(User::getCreateTime);

        return userMapper.selectPage(page, wrapper);
    }

    @Override
    public void updateUserStatusByAdmin(Long userId, Integer status, Integer verified, Integer role) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        // ✅ 只更新允许的字段，保护用户名、手机号等敏感信息不被篡改
        if (status != null) user.setStatus(status);
        if (verified != null) user.setVerified(verified);
        if (role != null) user.setRole(role);

        user.setUpdateTime(LocalDateTime.now());
        userMapper.updateById(user);
    }

    /*
    * 管理员删除用户
    * */
    @Override
    public void deleteUserByAdmin(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        // ✅ 方案二：使用 UpdateWrapper 强制更新 deleted 字段
        // 原理：直接生成 UPDATE user SET deleted = 1, update_time = ... WHERE id = ?
        LambdaUpdateWrapper<User> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(User::getId, userId)
                .set(User::getDeleted, 1)
                .set(User::getUpdateTime, LocalDateTime.now());

        int rows = userMapper.update(null, updateWrapper);

        if (rows <= 0) {
            throw new BusinessException("删除失败");
        }

        log.info("用户已被逻辑删除: userId={}", userId);
    }

}
