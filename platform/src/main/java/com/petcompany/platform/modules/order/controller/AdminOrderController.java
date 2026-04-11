package com.petcompany.platform.modules.order.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.petcompany.platform.common.result.Result;
import com.petcompany.platform.modules.order.dto.OrderResponse;
import com.petcompany.platform.modules.order.service.OrderService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String keyword
    ) {
        log.info("管理员查询订单: page={}, status={}, keyword={}", page, status, keyword);
        Page<OrderResponse> orderPage = orderService.getAdminOrderPage(page, size, status, keyword);
        return Result.success("获取订单列表成功", orderPage);
    }
    /**
     * ✅ 管理员获取订单详情
     */
    @GetMapping("/{id}")
    public Result<OrderResponse> getOrderDetail(@PathVariable Long id) {
        return Result.success("获取详情成功", orderService.getOrderDetail(id));
    }
}
