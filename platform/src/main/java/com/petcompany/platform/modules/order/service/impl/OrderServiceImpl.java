package com.petcompany.platform.modules.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.petcompany.platform.common.exception.BusinessException;
import com.petcompany.platform.common.service.AsyncService;
import com.petcompany.platform.infrastructure.security.CustomUserDetails;
import com.petcompany.platform.modules.order.dto.OrderCreateRequest;
import com.petcompany.platform.modules.order.dto.OrderResponse;
import com.petcompany.platform.modules.order.entity.Order;
import com.petcompany.platform.modules.order.service.OrderService;
import com.petcompany.platform.modules.order.mapper.OrderMapper;
import com.petcompany.platform.modules.pet.entity.Pet;
import com.petcompany.platform.modules.pet.service.PetService;
import com.petcompany.platform.modules.service.entity.ServiceType;
import com.petcompany.platform.modules.service.service.ServiceTypeService;
import com.petcompany.platform.modules.user.entity.User;
import com.petcompany.platform.modules.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单服务实现类
 */
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private PetService petService;

    @Resource
    private ServiceTypeService serviceTypeService;

    @Resource
    private AsyncService asyncService;
    @Resource
    private UserService userService;


    /*
    * 获取用户订单信息
    * */
    @Override
    public List<OrderResponse> getOrderList() {
        // 查询用户订单列表
        LambdaQueryWrapper<Order> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Order::getDeleted, 0);
        wrapper.orderByDesc(Order::getCreateTime);

        List<Order> orders = orderMapper.selectList(wrapper);
        return orders.stream().map(this::convertToResponse).collect(Collectors.toList());
    }


    @Override
    // ✅ 2. 新增：根据用户 ID 获取订单列表的方法
    public List<OrderResponse> getOrderListByUserId(Long userId) {
        LambdaQueryWrapper<Order> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Order::getUserId, userId);
        wrapper.eq(Order::getDeleted, 0);
        wrapper.orderByDesc(Order::getCreateTime);

        List<Order> orders = orderMapper.selectList(wrapper);
        return orders.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    @Override
    public OrderResponse getOrderDetail(Long id) {
        Order order = orderMapper.selectById(id);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        return convertToResponse(order);
    }

    @Override
    public Order getOrderById(Long id) {
        return orderMapper.selectById(id);
    }

    @Override
    public OrderResponse createOrder(OrderCreateRequest request) {
        // ✅ 1. 从安全上下文中获取当前登录用户的 ID
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BusinessException("用户未登录");
        }
        Long currentUserId;
        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomUserDetails) {
            currentUserId = ((CustomUserDetails) principal).getUserId();
        } else {
            throw new BusinessException("无法获取当前用户信息");
        }

        // 验证宠物是否存在
        Pet pet = petService.getPetById(request.getPetId());
        if (pet == null) {
            throw new BusinessException("宠物不存在");
        }

        // 验证服务类型是否存在
        ServiceType serviceType = serviceTypeService.getServiceTypeById(request.getServiceType());
        if (serviceType == null) {
            throw new BusinessException("服务类型不存在");
        }

        try {
            // ✅ 2. 补全：日期和时间解析逻辑
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

            LocalDate date = LocalDate.parse(request.getServiceDate(), dateFormatter);
            LocalTime startTime = LocalTime.parse(request.getServiceStartTime(), timeFormatter);
            LocalTime endTime = LocalTime.parse(request.getServiceEndTime(), timeFormatter);

            LocalDateTime serviceStart = LocalDateTime.of(date, startTime);
            LocalDateTime serviceEnd = LocalDateTime.of(date, endTime);

            // 处理跨天逻辑（如果结束时间小于开始时间，视为第二天）
            if (serviceEnd.isBefore(serviceStart)) {
                serviceEnd = serviceEnd.plusDays(1);
            }

            // 创建订单
            Order order = new Order();
            order.setUserId(currentUserId); // ✅ 3. 使用真实的用户 ID
            order.setPetId(request.getPetId());
            order.setServiceTypeId(request.getServiceType());
            order.setServiceStartTime(serviceStart);
            order.setServiceEndTime(serviceEnd);
            order.setAddress(request.getAddress());
            order.setPhone(request.getPhone());
            order.setNotes(request.getNotes());

            // ✅ 4. 价格处理
            if (request.getPrice() != null) {
                order.setPrice(request.getPrice().doubleValue());
            } else {
                throw new BusinessException("订单价格不能为空");
            }

            order.setStatus(1); // ✅ 修改：1-待支付
            order.setPayStatus(0);
            order.setOrderNo(generateOrderNo());
            order.setCreateTime(LocalDateTime.now());
            order.setUpdateTime(LocalDateTime.now());
            order.setDeleted(0);

            // 保存订单
            orderMapper.insert(order);
            log.info("用户 {} 创建订单 {}，金额：{}", currentUserId, order.getOrderNo(), request.getPrice());

            // 异步处理
            asyncService.processOrder(order.getId(), currentUserId);
            asyncService.sendNotification(currentUserId, "您的订单" + order.getOrderNo() + "已创建成功");

            return convertToResponse(order);
        } catch (Exception e) {
            log.error("创建订单发生严重错误", e);
            throw new BusinessException("创建订单失败: " + e.getMessage());
        }
    }

    @Override
    public OrderResponse cancelOrder(Long id) {
        // 获取订单
        Order order = orderMapper.selectById(id);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }

        // ✅ 修改：允许取消“待支付”或“待接单”状态的订单
        if (order.getStatus() != 1 && order.getStatus() != 2) {
            throw new BusinessException("当前订单状态不允许取消");
        }

        // 更新订单状态
        order.setStatus(6); // 已取消
        order.setUpdateTime(LocalDateTime.now());
        orderMapper.updateById(order);
        log.info("取消订单{}", id);

        return convertToResponse(order);
    }


    /*
    * 接取订单
    * */
    @Override
    public OrderResponse acceptOrder(Long id) {
        // 获取订单
        Order order = orderMapper.selectById(id);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }

        // 验证订单状态
        if (order.getStatus() != 2) { // 只有待接单状态可以接受
            throw new BusinessException("订单状态不允许接受");
        }


        // ✅ 获取当前登录的服务者ID
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BusinessException("用户未登录");
        }
        Long providerId;
        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomUserDetails) {
            providerId = ((CustomUserDetails) principal).getUserId();
        } else {
            throw new BusinessException("无法获取当前用户信息");
        }

        // ✅ 更新状态为 3-已接单，并绑定服务者
        order.setStatus(3); // ✅ 修改：3-已接单
        order.setProviderId(providerId);
        order.setUpdateTime(LocalDateTime.now());
        orderMapper.updateById(order);
        log.info("服务者 {} 接取订单 {}", providerId, id);

        return convertToResponse(order);
    }

    @Override
    public OrderResponse startOrder(Long id) {
        // 获取订单
        Order order = orderMapper.selectById(id);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }

        // ✅ 只有“已接单”状态才能开始服务
        if (order.getStatus() != 3) {
            throw new BusinessException("请先接取订单后再开始服务");
        }

        // ✅ 更新状态为 4-服务中
        order.setStatus(4); // ✅ 修改：4-服务中
        order.setUpdateTime(LocalDateTime.now());
        orderMapper.updateById(order);
        log.info("开始服务订单 {}", id);

        return convertToResponse(order);
    }

    @Override
    public OrderResponse completeOrder(Long id) {
        // 获取订单
        Order order = orderMapper.selectById(id);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }

        // 验证订单状态
        if (order.getStatus() != 4) { // 只有服务中状态可以完成服务
            throw new BusinessException("订单状态不允许完成服务");
        }

        // 更新订单状态
        order.setStatus(5); // 待评价
        order.setUpdateTime(LocalDateTime.now());
        orderMapper.updateById(order);
        log.info("完成服务{}", id);

        return convertToResponse(order);
    }




    /*
    * 服务者获取接取后订单列表
    * */
    @Override
    public List<OrderResponse> getProviderOrderList() {
        // ✅ 1. 从安全上下文中获取当前登录用户的 ID (服务者ID)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BusinessException("用户未登录");
        }
        Long providerId;
        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomUserDetails) {
            providerId = ((CustomUserDetails) principal).getUserId();
        } else {
            throw new BusinessException("无法获取当前用户信息");
        }

        // ✅ 2. 查询该服务者接取的、且状态属于【已接单、服务中、已完成】的订单
        LambdaQueryWrapper<Order> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Order::getProviderId, providerId);

        // 3-已接单, 4-服务中, 5-已完成
        wrapper.in(Order::getStatus, 3, 4, 5);
        wrapper.eq(Order::getDeleted, 0);
        wrapper.orderByDesc(Order::getCreateTime);


        List<Order> orders = orderMapper.selectList(wrapper);
        return orders.stream().map(this::convertToResponse).collect(Collectors.toList());
    }


    @Override
    public List<OrderResponse> getPendingOrderList() {
        // 查询待接单的订单列表
        LambdaQueryWrapper<Order> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Order::getStatus, 2); // 待接单
        wrapper.eq(Order::getDeleted, 0);
        wrapper.orderByDesc(Order::getCreateTime);

        List<Order> orders = orderMapper.selectList(wrapper);
        return orders.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    /**
     * 生成订单号
     */
    private String generateOrderNo() {
        return "ORD" + System.currentTimeMillis() + (int) (Math.random() * 1000);
    }

    /**
     * 转换为响应对象
     */
    private OrderResponse convertToResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setOrderNo(order.getOrderNo());
        response.setAddress(order.getAddress());
        response.setPhone(order.getPhone());
        response.setNotes(order.getNotes());
        // ✅ 修改：直接设置 Double 或转换为 BigDecimal，不要调用 .intValue()
        if (order.getPrice() != null) {
            response.setPrice(java.math.BigDecimal.valueOf(order.getPrice()));
        }
        response.setCreateTime(order.getCreateTime());
        response.setReviewed(false);


        // ✅ 2. 获取并设置用户名（下单用户）
        if (order.getUserId() != null) {
            User user = userService.getUserById(order.getUserId());
            if (user != null) {
                response.setUserName(user.getUsername() != null ? user.getUsername() : user.getUsername());
                // 注意：OrderResponse 里目前没有 userName 字段，我们可以暂时复用 providerName 或者你在 DTO 里加一个 userName 字段
                // 建议：在 OrderResponse.java 中增加 private String userName;
            }
        }

        // ✅ 3. 如果有服务提供者，也可以顺便查出服务者的名字
        if (order.getProviderId() != null) {
            User provider = userService.getUserById(order.getProviderId());
            if (provider != null) {
                response.setProviderName(provider.getUsername() != null ? provider.getUsername() : provider.getUsername());
            }
        }

        // 获取宠物信息
        Pet pet = petService.getPetById(order.getPetId());
        if (pet != null) {
            response.setPetName(pet.getName());
        }

        // 获取服务类型信息
        ServiceType serviceType = serviceTypeService.getServiceTypeById(order.getServiceTypeId());
        if (serviceType != null) {
            response.setServiceName(serviceType.getName());
        }

        // 格式化服务时间
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String serviceTime = order.getServiceStartTime().format(formatter) + "-" + order.getServiceEndTime().format(formatter);
        response.setServiceTime(serviceTime);

        // ✅ 更新状态映射逻辑
        switch (order.getStatus()) {
            case 1:
                response.setStatus("PENDING_PAYMENT");
                response.setStatusText("待支付");
                break;
            case 2:
                response.setStatus("PENDING");
                response.setStatusText("待接单");
                break;
            case 3:
                response.setStatus("ACCEPTED"); // ✅ 新增：已接单
                response.setStatusText("已接单");
                break;
            case 4:
                response.setStatus("IN_SERVICE");
                response.setStatusText("服务中");
                break;
            case 5:
                response.setStatus("COMPLETED");
                response.setStatusText("已完成");
                break;
            case 6:
                response.setStatus("CANCELLED");
                response.setStatusText("已取消");
                break;
            default:
                response.setStatus("UNKNOWN");
                response.setStatusText("未知状态");
        }

        // 设置支付状态
        switch (order.getPayStatus()) {
            case 0:
                response.setPaymentStatusText("待支付");
                break;
            case 1:
                response.setPaymentStatusText("已支付");
                break;
            case 2:
                response.setPaymentStatusText("退款中");
                break;
            case 3:
                response.setPaymentStatusText("已退款");
                break;
            default:
                response.setPaymentStatusText("未知状态");
        }

        return response;
    }
    @Override
    public Long countByProviderIdAndStatus(Long providerId, Integer status) {
        LambdaQueryWrapper<Order> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Order::getProviderId, providerId)
                .eq(Order::getStatus, status)
                .eq(Order::getDeleted, 0);
        return orderMapper.selectCount(wrapper);
    }


    /*
    * 管理员获取订单列表
    * */
    @Override
    public Page<OrderResponse> getAdminOrderPage(int page, int size, String status, Long serviceTypeId, String keyword) {
        Page<Order> mpPage = new Page<>(page, size);
        LambdaQueryWrapper<Order> wrapper = Wrappers.lambdaQuery();

        // 1. 筛选状态 (需要将前端的 String 映射回数据库的 Integer)
        if (status != null && !status.isEmpty()) {
            Integer dbStatus = mapStatusStringToInt(status);
            if (dbStatus != null) {
                wrapper.eq(Order::getStatus, dbStatus);
            }
        }

        // 2. 筛选服务类型
        if (serviceTypeId != null) {
            wrapper.eq(Order::getServiceTypeId, serviceTypeId);
        }

        // 3. 关键词搜索（搜索订单号或手机号）
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(Order::getOrderNo, keyword)
                    .or()
                    .like(Order::getPhone, keyword));
        }

        wrapper.eq(Order::getDeleted, 0);
        wrapper.orderByDesc(Order::getCreateTime);

        // 执行分页查询
        Page<Order> orderPage = orderMapper.selectPage(mpPage, wrapper);

        // 转换为 DTO 分页对象
        Page<OrderResponse> responsePage = new Page<>();
        responsePage.setCurrent(orderPage.getCurrent());
        responsePage.setSize(orderPage.getSize());
        responsePage.setTotal(orderPage.getTotal());
        responsePage.setRecords(orderPage.getRecords().stream().map(this::convertToResponse).collect(Collectors.toList()));

        return responsePage;
    }

    /**
     * ✅ 辅助方法：将前端的状态字符串映射为数据库的 Integer
     */
    private Integer mapStatusStringToInt(String statusStr) {
        switch (statusStr) {
            case "PENDING_PAYMENT": return 1;
            case "PENDING": return 2;
            case "IN_SERVICE": return 3; // 或者是 4，取决于你具体的状态定义
            case "COMPLETED": return 5;
            case "CANCELLED": return 6;
            default: return null;
        }
    }
    /*
     * 当支付成功更改订单状态
     * */
    @Override
    public void updateOrderStatusAfterPay(Long orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order != null) {
            // 只有处于“待支付”状态的订单才能转为“待接单”
            if (order.getStatus() == 1) {
                order.setStatus(2); // 2-待接单
                order.setPayStatus(1); // 1-已支付
                order.setUpdateTime(LocalDateTime.now());
                orderMapper.updateById(order);
                log.info("订单 {} 支付成功，状态已更新为待接单", orderId);
            }
        }
    }

    /*
     * 获取服务者订单统计数据
     * */
    @Override
    public java.util.Map<String, Object> getProviderStats() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BusinessException("用户未登录");
        }

        // ✅ 2. 修复：从 CustomUserDetails 中获取 ID，而不是直接强转 principal
        Long providerId;
        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomUserDetails) {
            providerId = ((CustomUserDetails) principal).getUserId();
        } else if (principal instanceof Long) {
            providerId = (Long) principal; // 兼容旧逻辑
        } else {
            throw new BusinessException("无法获取当前用户信息");
        }

        java.util.Map<String, Object> stats = new java.util.HashMap<>();

        // 1. 待处理订单 (已接单 + 服务中)
        long pendingCount = countByProviderIdAndStatus(providerId, 3) +
                countByProviderIdAndStatus(providerId, 4);
        stats.put("pendingOrders", pendingCount);

        // 2. 已完成订单
        long completedCount = countByProviderIdAndStatus(providerId, 5);
        stats.put("completedOrders", completedCount);

        // 3. 总收入 (简单累加已完成订单的价格)
        LambdaQueryWrapper<Order> earningWrapper = Wrappers.lambdaQuery();
        earningWrapper.eq(Order::getProviderId, providerId)
                .eq(Order::getStatus, 5) // 只算完成的
                .eq(Order::getDeleted, 0);
        List<Order> completedOrders = orderMapper.selectList(earningWrapper);
        double totalEarnings = completedOrders.stream()
                .mapToDouble(o -> o.getPrice() != null ? o.getPrice() : 0.0)
                .sum();
        stats.put("totalEarnings", totalEarnings);

        // 4. 平均评分 (暂时给个默认值，后续可以对接评价表)
        stats.put("averageRating", 4.8);

        return stats;
    }

}
