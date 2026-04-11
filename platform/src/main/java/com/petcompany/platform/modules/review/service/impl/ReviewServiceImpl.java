package com.petcompany.platform.modules.review.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.petcompany.platform.common.exception.BusinessException;

import com.petcompany.platform.modules.order.entity.Order;
import com.petcompany.platform.modules.order.service.OrderService;

import com.petcompany.platform.modules.review.dto.ReviewResponse;
import com.petcompany.platform.modules.review.entity.Review;
import com.petcompany.platform.modules.review.service.ReviewService;
import com.petcompany.platform.modules.review.mapper.ReviewMapper;

import com.petcompany.platform.modules.user.entity.User;
import com.petcompany.platform.modules.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;


import java.time.LocalDateTime;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 评价服务实现类
 */
@Service
@Slf4j
public class ReviewServiceImpl implements ReviewService {

    @Resource
    private ReviewMapper reviewMapper;

    @Resource
    private OrderService orderService;

    @Resource
    private UserService userService;

    @Override
    public Review createReview(Long userId, Long orderId, Integer score, String content, String images) {
        // 获取订单
        Order order = orderService.getOrderById(orderId);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }

        // 验证订单是否属于当前用户
        if (!order.getUserId().equals(userId)) {
            throw new BusinessException("订单不属于当前用户");
        }

        // 验证订单状态（假设 5 是已完成/待评价状态）
        if (order.getStatus() != 5) {
            throw new BusinessException("订单状态不允许评价");
        }

        // 检查是否已经评价过
        LambdaQueryWrapper<Review> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Review::getOrderId, orderId);
        Review existing = reviewMapper.selectOne(wrapper);
        if (existing != null) {
            throw new BusinessException("该订单已经评价过");
        }

        // 创建评价
        Review review = new Review();
        review.setOrderId(orderId);
        review.setUserId(userId);
        review.setProviderId(order.getProviderId());

        // ✅ 修正：使用 setRating 匹配实体类属性
        review.setRating(score);

        review.setContent(content);
        review.setImages(images);
        review.setCreateTime(LocalDateTime.now());
        review.setUpdateTime(LocalDateTime.now());
        review.setDeleted(0);

        // 保存评价
        reviewMapper.insert(review);
        log.info("用户{}为订单{}创建评价，评分：{}", userId, orderId, score);

        return review;
    }

    @Override
    public void replyReview(Long providerId, Long reviewId, String reply) {
        // 获取评价
        Review review = reviewMapper.selectById(reviewId);
        if (review == null) {
            throw new BusinessException("评价不存在");
        }

        // 验证评价是否属于当前服务提供者
        if (!review.getProviderId().equals(providerId)) {
            throw new BusinessException("评价不属于当前服务提供者");
        }

        // ✅ 暂时注释掉回复逻辑，因为实体类和数据库里还没有 reply 字段
        /*
        review.setReply(reply);
        review.setUpdateTime(LocalDateTime.now());
        reviewMapper.updateById(review);
        */
        log.info("服务提供者{}尝试回复评价{}（功能待开发）", providerId, reviewId);
    }

    @Override
    public Review getReviewById(Long id) {
        return reviewMapper.selectById(id);
    }

    @Override
    public Review getReviewByOrderId(Long orderId) {
        LambdaQueryWrapper<Review> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Review::getOrderId, orderId);
        return reviewMapper.selectOne(wrapper);
    }

    @Override
    public List<ReviewResponse> getUserReviewList(Long userId) {
        // 查询用户的评价列表
        LambdaQueryWrapper<Review> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Review::getUserId, userId);
        wrapper.eq(Review::getDeleted, 0);
        wrapper.orderByDesc(Review::getCreateTime);

        List<Review> reviews = reviewMapper.selectList(wrapper);
        return reviews.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    @Override
    public List<ReviewResponse> getProviderReviewList(Long providerId) {
        // 查询服务提供者的评价列表
        LambdaQueryWrapper<Review> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Review::getProviderId, providerId);
        wrapper.eq(Review::getDeleted, 0);
        wrapper.orderByDesc(Review::getCreateTime);

        List<Review> reviews = reviewMapper.selectList(wrapper);
        return reviews.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    /*
    * 统计未删除的评价总数
    */
    @Override
    public long countAllReview() {
        LambdaQueryWrapper<Review> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Review::getDeleted, 0);
        return reviewMapper.selectCount(wrapper);
    }

    /**
     * 转换为响应对象
     */
    private ReviewResponse convertToResponse(Review review) {
        ReviewResponse response = new ReviewResponse();
        response.setId(review.getId());
        response.setOrderId(review.getOrderId());
        response.setUserId(review.getUserId());
        response.setProviderId(review.getProviderId());

        // ✅ 修正：使用 getRating 替代 getScore
        response.setScore(review.getRating());

        response.setContent(review.getContent());
        response.setImages(review.getImages());

        // ✅ 修正：如果以后加了 reply 字段再开启
        // response.setReply(review.getReply());

        response.setCreateTime(review.getCreateTime());

        // 获取用户信息
        User user = userService.getUserById(review.getUserId());
        if (user != null) {
            response.setUserName(user.getUsername());
        }

        // 获取服务提供者信息
        User provider = userService.getUserById(review.getProviderId());
        if (provider != null) {
            response.setProviderName(provider.getUsername());
        }

        return response;
    }

}
