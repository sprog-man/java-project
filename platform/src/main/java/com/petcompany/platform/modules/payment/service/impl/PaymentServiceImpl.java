package com.petcompany.platform.modules.payment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.petcompany.platform.common.exception.BusinessException;
import com.petcompany.platform.modules.order.entity.Order;
import com.petcompany.platform.modules.order.service.OrderService;
import com.petcompany.platform.modules.payment.dto.PaymentResponse;
import com.petcompany.platform.modules.payment.entity.PaymentRecord;
import com.petcompany.platform.modules.payment.service.PaymentService;
import com.petcompany.platform.modules.payment.mapper.PaymentRecordMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 支付服务实现类
 */
@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    @Resource
    private PaymentRecordMapper paymentRecordMapper;

    @Resource
    private OrderService orderService;

    @Override
    public PaymentRecord createPayment(Long userId, Long orderId, Integer payType) {
        // 获取订单
        Order order = orderService.getOrderById(orderId);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }

        // 验证订单是否属于当前用户
        if (!order.getUserId().equals(userId)) {
            throw new BusinessException("订单不属于当前用户");
        }

        // 验证订单状态
        if (order.getStatus() != 1) { // 只有待支付状态可以支付
            throw new BusinessException("订单状态不允许支付");
        }

        // 检查是否已经创建过支付记录
        LambdaQueryWrapper<PaymentRecord> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(PaymentRecord::getOrderId, orderId);
        PaymentRecord existing = paymentRecordMapper.selectOne(wrapper);
        if (existing != null) {
            return existing;
        }

        // 创建支付记录
        PaymentRecord paymentRecord = new PaymentRecord();
        paymentRecord.setOrderId(orderId);
        paymentRecord.setAmount(order.getPrice());
        paymentRecord.setStatus(0); // 待支付
        paymentRecord.setPayType(payType);
        paymentRecord.setPayOrderNo(generatePayOrderNo());
        paymentRecord.setCreateTime(LocalDateTime.now());
        paymentRecord.setUpdateTime(LocalDateTime.now());
        paymentRecord.setDeleted(0);

        // 保存支付记录
        paymentRecordMapper.insert(paymentRecord);
        log.info("用户{}为订单{}创建支付记录，金额：{}", userId, orderId, order.getPrice());

        // TODO: 调用第三方支付接口，生成支付链接或二维码

        return paymentRecord;
    }

    @Override
    public void handlePaymentCallback(String payOrderNo, String tradeNo, Integer status) {
        // 根据支付单号查询支付记录
        LambdaQueryWrapper<PaymentRecord> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(PaymentRecord::getPayOrderNo, payOrderNo);
        PaymentRecord paymentRecord = paymentRecordMapper.selectOne(wrapper);
        if (paymentRecord == null) {
            throw new BusinessException("支付记录不存在");
        }

        // 更新支付记录
        paymentRecord.setStatus(status);
        paymentRecord.setTradeNo(tradeNo);
        if (status == 1) { // 支付成功
            paymentRecord.setPayTime(LocalDateTime.now());
            // 更新订单状态
            Order order = orderService.getOrderById(paymentRecord.getOrderId());
            if (order != null) {
                // TODO: 更新订单状态为已支付
            }
        }
        paymentRecord.setUpdateTime(LocalDateTime.now());
        paymentRecordMapper.updateById(paymentRecord);
        log.info("处理支付回调，支付单号：{}，状态：{}", payOrderNo, status);
    }

    @Override
    public PaymentRecord getPaymentById(Long id) {
        return paymentRecordMapper.selectById(id);
    }

    @Override
    public PaymentRecord getPaymentByOrderId(Long orderId) {
        LambdaQueryWrapper<PaymentRecord> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(PaymentRecord::getOrderId, orderId);
        return paymentRecordMapper.selectOne(wrapper);
    }

    @Override
    public List<PaymentResponse> getUserPaymentList(Long userId) {
        // 查询用户的支付记录
        // 这里需要关联订单表，获取用户的订单，然后查询对应的支付记录
        // 简化实现，直接返回所有支付记录
        List<PaymentRecord> paymentRecords = paymentRecordMapper.selectList(null);
        return paymentRecords.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    /**
     * 生成支付单号
     */
    private String generatePayOrderNo() {
        return "PAY" + System.currentTimeMillis() + (int) (Math.random() * 1000);
    }

    /**
     * 转换为响应对象
     */
    private PaymentResponse convertToResponse(PaymentRecord paymentRecord) {
        PaymentResponse response = new PaymentResponse();
        response.setId(paymentRecord.getId());
        response.setOrderId(paymentRecord.getOrderId());
        response.setAmount(paymentRecord.getAmount());
        response.setStatus(paymentRecord.getStatus());
        response.setPayType(paymentRecord.getPayType());
        response.setPayOrderNo(paymentRecord.getPayOrderNo());
        response.setTradeNo(paymentRecord.getTradeNo());
        response.setPayTime(paymentRecord.getPayTime());
        response.setCreateTime(paymentRecord.getCreateTime());
        return response;
    }

}
