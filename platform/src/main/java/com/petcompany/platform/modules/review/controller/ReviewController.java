package com.petcompany.platform.modules.review.controller;

import com.petcompany.platform.common.result.Result;
import com.petcompany.platform.infrastructure.security.UserContext;
import com.petcompany.platform.modules.review.dto.ReviewCreateRequest;
import com.petcompany.platform.modules.review.dto.ReviewResponse;
import com.petcompany.platform.modules.review.service.ReviewService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import java.util.List;

/**
 * 评价控制器
 */
@RestController
@RequestMapping("/review")
@Slf4j
public class ReviewController {

    @Resource
    private ReviewService reviewService;

    /**
     * 创建评价
     */
    @PostMapping("/create")
    public Result<?> createReview(@Valid @RequestBody ReviewCreateRequest request) {
        Long userId = UserContext.getCurrentUserId();
        log.info("用户{}创建评价，订单ID：{}", userId, request.getOrderId());

        reviewService.createReview(userId, request.getOrderId(), request.getScore(), request.getContent(), request.getImages());
        return Result.success("评价创建成功");
    }

    /**
     * 回复评价
     */
    @PostMapping("/reply/{id}")
    public Result<?> replyReview(@PathVariable Long id, @RequestParam String reply) {
        Long providerId = UserContext.getCurrentUserId();
        log.info("服务提供者{}回复评价{}", providerId, id);

        reviewService.replyReview(providerId, id, reply);
        return Result.success("回复成功");
    }

    /**
     * 获取评价详情
     */
    @GetMapping("/{id}")
    public Result<?> getReviewById(@PathVariable Long id) {
        log.info("获取评价详情，ID：{}", id);

        return Result.success(reviewService.getReviewById(id));
    }

    /**
     * 根据订单ID获取评价
     */
    @GetMapping("/order/{orderId}")
    public Result<?> getReviewByOrderId(@PathVariable Long orderId) {
        log.info("根据订单ID获取评价，订单ID：{}", orderId);

        return Result.success(reviewService.getReviewByOrderId(orderId));
    }

    /**
     * 获取用户评价列表
     */
    @GetMapping("/user/list")
    public Result<List<ReviewResponse>> getUserReviewList() {
        Long userId = UserContext.getCurrentUserId();
        log.info("获取用户{}的评价列表", userId);

        List<ReviewResponse> list = reviewService.getUserReviewList(userId);
        return Result.success(list);
    }

    /**
     * 获取服务提供者评价列表
     */
    @GetMapping("/provider/list")
    public Result<List<ReviewResponse>> getProviderReviewList() {
        Long providerId = UserContext.getCurrentUserId();
        log.info("获取服务提供者{}的评价列表", providerId);

        List<ReviewResponse> list = reviewService.getProviderReviewList(providerId);
        return Result.success(list);
    }

}
