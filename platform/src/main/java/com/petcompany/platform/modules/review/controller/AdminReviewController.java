package com.petcompany.platform.modules.review.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.petcompany.platform.common.result.Result;
import com.petcompany.platform.infrastructure.security.annotation.RequiresPermission;
import com.petcompany.platform.modules.review.dto.ReviewResponse;
import com.petcompany.platform.modules.review.service.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import java.util.List;

/**
 * 管理员-评价管理控制器
 */
@RestController
@RequestMapping("/admin/reviews")
@Slf4j
public class AdminReviewController {
    @Resource
    private ReviewService reviewService;

    /**
     * 获取所有评价列表（支持分页和搜索）
     */
    @GetMapping
    @RequiresPermission(userTypes = {0,2}) // 仅限超级管理员
    public Result<Page<ReviewResponse>> getReviewList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword, // 搜索订单号或用户名
            @RequestParam(required = false) Integer rating   // 筛选评分
    ) {
        log.info("管理员获取评价列表: page={}, size={}, keyword={}, rating={}", page, size, keyword, rating);
        Page<ReviewResponse> reviewPage = reviewService.getAdminReviewPage(page, size, keyword, rating);
        return Result.success(reviewPage);
    }

    /**
     * 删除评价（逻辑删除）
     */
    @DeleteMapping("/{id}")
    @RequiresPermission(userTypes = {0})
    public Result<?> deleteReview(@PathVariable Long id) {
        log.info("管理员删除评价: id={}", id);
        reviewService.deleteReview(id);
        return Result.success("删除成功");
    }
}
