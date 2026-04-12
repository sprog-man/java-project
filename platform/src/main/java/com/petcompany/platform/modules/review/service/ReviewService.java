package com.petcompany.platform.modules.review.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.petcompany.platform.modules.review.dto.ReviewResponse;
import com.petcompany.platform.modules.review.entity.Review;

import java.util.List;

/**
 * 评价服务接口
 */
public interface ReviewService {

    /**
     * 创建评价
     */
    Review createReview(Long userId, Long orderId, Integer score, String content, String images);

    /**
     * 回复评价
     */
    void replyReview(Long providerId, Long reviewId, String reply);

    /**
     * 获取评价详情
     */
    Review getReviewById(Long id);

    /**
     * 根据订单ID获取评价
     */
    Review getReviewByOrderId(Long orderId);

    /**
     * 获取用户评价列表
     */
    List<ReviewResponse> getUserReviewList(Long userId);

    /**
     * 获取服务提供者评价列表
     */
    List<ReviewResponse> getProviderReviewList(Long providerId);

    /*
    * 获取管理员评价列表
    * */
    Page<ReviewResponse> getAdminReviewPage(int page, int size, String keyword, Integer rating);


    /*
    * 删除评价
    * */
    void deleteReview(Long id);
}
