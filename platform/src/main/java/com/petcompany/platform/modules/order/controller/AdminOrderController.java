package com.petcompany.platform.modules.order.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.petcompany.platform.common.result.Result;
import com.petcompany.platform.modules.order.dto.OrderResponse;
import com.petcompany.platform.modules.order.service.OrderService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/orders")
@Slf4j
public class AdminOrderController {
    @Resource
    private OrderService orderService;

    /**
     * ✅ 管理员获取订单列表（支持分页和筛选）
     * GET /admin/orders?page=1&size=10&status=1&keyword=xxx
     */
    @GetMapping
    public Result<Page<OrderResponse>> getOrderList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status, // ✅ 改为 String，因为前端传的是 PENDING_PAYMENT
            @RequestParam(required = false) Long serviceTypeId, // ✅ 新增：接收服务类型ID
            @RequestParam(required = false) String keyword
    ) {
        log.info("管理员查询订单: page={}, status={}, serviceTypeId={}, keyword={}", page, status, serviceTypeId, keyword);
        // ✅ 将前端传来的英文状态转换回数据库存的 Integer (如果后端逻辑需要)
        // 这里我们直接把转换逻辑交给 Service 层，或者在这里转好传过去

        Page<OrderResponse> orderPage = orderService.getAdminOrderPage(page, size, status, serviceTypeId, keyword);

        return Result.success("获取订单列表成功", orderPage);
    }
    /**
     * ✅ 管理员获取订单详情
     */
    @GetMapping("/{id}")
    public Result<OrderResponse> getOrderDetail(@PathVariable Long id) {
        return Result.success("获取详情成功", orderService.getOrderDetail(id));
    }

    /**
     * ✅ 新增：获取订单状态枚举列表（供前端筛选下拉框使用）
     */
    @GetMapping("/status-options")
    public Result<List<Map<String, Object>>> getStatusOptions() {
        List<Map<String, Object>> options = List.of(
                Map.of("value", "", "label", "全部状态"),
                Map.of("value", "PENDING_PAYMENT", "label", "待支付"),
                Map.of("value", "PENDING", "label", "待接单"),
                Map.of("value", "IN_SERVICE", "label", "服务中"),
                Map.of("value", "COMPLETED", "label", "已完成"),
                Map.of("value", "CANCELLED", "label", "已取消")
        );
        return Result.success(options);
    }
}
