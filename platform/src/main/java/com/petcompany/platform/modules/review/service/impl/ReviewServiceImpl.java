package com.petcompany.platform.modules.review.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.petcompany.platform.common.exception.BusinessException;

import com.petcompany.platform.modules.order.entity.Order;
import com.petcompany.platform.modules.order.service.OrderService;

import com.petcompany.platform.modules.review.dto.ReviewResponse;
import com.petcompany.platform.modules.review.entity.Review;
import com.petcompany.platform.modules.review.service.ReviewService;
import com.petcompany.platform.modules.review.mapper.ReviewMapper;

import com.petcompany.platform.modules.service.entity.ServiceType;
import com.petcompany.platform.modules.service.service.ServiceTypeService;
import com.petcompany.platform.modules.user.entity.User;
import com.petcompany.platform.modules.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;

import java.time.format.DateTimeFormatter;
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

    @Resource
    private ServiceTypeService serviceTypeService; // ✅ 注入 ServiceTypeService


    @Override
    @Transactional
    public Review createReview(Long userId, Long orderId, Integer score, String content, String images) {
        // 1. 获取订单
        Order order = orderService.getOrderById(orderId);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }

        // 2. 验证权限：必须是下单用户本人
        if (!order.getUserId().equals(userId)) {
            throw new BusinessException("订单不属于当前用户");
        }

        // 3. 验证状态：只有状态为 5 (已完成) 才能评价
        if (order.getStatus() != 5) {
            throw new BusinessException("订单状态不允许评价");
        }

        // 4. 幂等性检查：防止重复评价
        LambdaQueryWrapper<Review> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Review::getOrderId, orderId);
        Review existing = reviewMapper.selectOne(wrapper);
        if (reviewMapper.selectCount(wrapper) > 0) {
            throw new BusinessException("该订单已评价，请勿重复操作");
        }

        // 创建评价
        Review review = new Review();
        review.setOrderId(orderId);
        review.setUserId(userId);
        review.setProviderId(order.getProviderId());
        review.setTargetId(order.getProviderId()); // ✅ 明确被评价人是服务者


        // ✅ 修正：使用 setRating 匹配实体类属性
        review.setRating(score);

        review.setContent(content);
        review.setImages(images);
        review.setCreateTime(LocalDateTime.now());
        review.setUpdateTime(LocalDateTime.now());
        review.setDeleted(0);

        // 保存评价
        reviewMapper.insert(review);

        // 核心联动：更新订单的评价状态为 1 (已评价)防止订单重复评价
        order.setReviewStatus(1);
        orderService.updateOrder(order);
        log.info("用户{}为订单{}创建评价，评分：{}", userId, orderId, score);

        return review;
    }


    /*
    * 删除评论
    * */
    @Override
    @Transactional
    public void deleteReview(Long id) {
        Review review = reviewMapper.selectById(id);
        if (review != null) {
            review.setDeleted(1);
            review.setUpdateTime(LocalDateTime.now());
            reviewMapper.updateById(review);

            // 如果删除了评价，记得把订单的 review_status 改回 0
            Order order = orderService.getOrderById(review.getOrderId());
            if (order != null) {
                order.setReviewStatus(0);
                orderService.updateOrder(order);
            }
        }
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


        // ✅ 核心联动：获取并填充订单相关信息
        Order order = orderService.getOrderById(review.getOrderId());
        if (order != null) {
            // 1. 设置订单号
            response.setOrderNo(order.getOrderNo());

            // 2. 获取服务名称
            if (order.getServiceTypeId() != null) {
                ServiceType serviceType = serviceTypeService.getServiceTypeById(order.getServiceTypeId());
                if (serviceType != null) {
                    response.setServiceName(serviceType.getName());
                }
            }

            // 3. 格式化服务时间
            if (order.getServiceStartTime() != null && order.getServiceEndTime() != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                String serviceTime = order.getServiceStartTime().format(formatter) + "-" +
                        order.getServiceEndTime().format(formatter);
                response.setServiceTime(serviceTime);
            }
        }



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



    /*
    * 管理员获取用户评价列表
    * */
    @Override
    public Page<ReviewResponse> getAdminReviewPage(int page, int size, String keyword, Integer rating) {
        Page<Review> mpPage = new Page<>(page, size);
        LambdaQueryWrapper<Review> wrapper = Wrappers.lambdaQuery();

        // 1. 评分筛选
        if (rating != null && rating > 0) {
            wrapper.eq(Review::getRating, rating);
        }

        // 2. 关键词搜索（修复版）
        if (keyword != null && !keyword.isEmpty()) {
            List<Long> targetOrderIds = new java.util.ArrayList<>();

            // ✅ 现在可以正常调用 orderService.list() 了
            List<Order> ordersByNo = orderService.list(Wrappers.lambdaQuery(Order.class)
                    .like(Order::getOrderNo, keyword));

            if (!ordersByNo.isEmpty()) {
                targetOrderIds.addAll(ordersByNo.stream().map(Order::getId).collect(Collectors.toList()));
            }

            // 尝试按用户名搜索 (先找人，再找单)
            List<User> users = userService.list(Wrappers.lambdaQuery(User.class)
                    .like(User::getUsername, keyword)
                    .or().like(User::getNickname, keyword));

            if (!users.isEmpty()) {
                List<Long> userIds = users.stream().map(User::getId).collect(Collectors.toList());
                // ✅ 同样使用 orderService.list()
                List<Order> ordersByUser = orderService.list(Wrappers.lambdaQuery(Order.class)
                        .in(Order::getUserId, userIds));

                if (!ordersByUser.isEmpty()) {
                    targetOrderIds.addAll(ordersByUser.stream().map(Order::getId).collect(Collectors.toList()));
                }
            }


            if (targetOrderIds.isEmpty()) {
                // 如果没找到对应的订单，直接返回空页，防止全表扫描
                return new Page<>(page, size, 0);
            }

            // 使用 distinct 防止重复 ID
            List<Long> distinctOrderIds = targetOrderIds.stream().distinct().collect(Collectors.toList());
            wrapper.in(Review::getOrderId, distinctOrderIds);
        }

        wrapper.eq(Review::getDeleted, 0);
        wrapper.orderByDesc(Review::getCreateTime);

        Page<Review> reviewPage = reviewMapper.selectPage(mpPage, wrapper);

        // 转换为 DTO 分页对象
        Page<ReviewResponse> responsePage = new Page<>();
        responsePage.setCurrent(reviewPage.getCurrent());
        responsePage.setSize(reviewPage.getSize());
        responsePage.setTotal(reviewPage.getTotal());

        // ✅ 转换数据并填充详情
        responsePage.setRecords(reviewPage.getRecords().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList()));


        return responsePage;
    }

}
