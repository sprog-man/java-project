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
        if (order.getStatus() != 1 && order.getStatus() != 2) {
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
        paymentRecord.setUserId(userId); // ✅ 补全用户ID
        paymentRecord.setAmount(order.getPrice());

        // ✅ 设置支付方式 (假设 1-微信, 2-支付宝，转为字符串存储)
        paymentRecord.setPaymentMethod(payType == 1 ? "WECHAT" : "ALIPAY");

        // ✅ 生成内部支付单号
        String payOrderNo = generatePayOrderNo();
        paymentRecord.setPayOrderNo(payOrderNo);

        paymentRecord.setStatus(0); // 待支付
        paymentRecord.setCreateTime(LocalDateTime.now());
        paymentRecord.setUpdateTime(LocalDateTime.now());
        paymentRecord.setDeleted(0);

        // 保存支付记录
        paymentRecordMapper.insert(paymentRecord);
        log.info("用户{}为订单{}创建支付记录，单号：{}，金额：{}", userId, orderId, payOrderNo, order.getPrice());

        return paymentRecord;
    }

    @Override
    public void handlePaymentCallback(String payOrderNo, String tradeNo, Integer status) {
        // 根据内部支付单号查询支付记录
        LambdaQueryWrapper<PaymentRecord> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(PaymentRecord::getPayOrderNo, payOrderNo);
        PaymentRecord paymentRecord = paymentRecordMapper.selectOne(wrapper);
        if (paymentRecord == null) {
            throw new BusinessException("支付记录不存在");
        }

        // ✅ 幂等性处理：如果已经是成功状态，直接返回
        if (paymentRecord.getStatus() == 1) {
            log.info("支付记录 {} 已处理过，忽略重复回调", payOrderNo);
            return;
        }

        // 更新支付记录
        paymentRecord.setStatus(status);
        paymentRecord.setTransactionId(tradeNo); // ✅ 修正：使用 setTransactionId

        if (status == 1) { // 支付成功
            paymentRecord.setPayTime(LocalDateTime.now());

            // ✅ 更新订单状态为已支付/待接单
            Order order = orderService.getOrderById(paymentRecord.getOrderId());
            if (order != null) {
                order.setStatus(2); // 2-待接单
                order.setPayStatus(1); // 1-已支付
                // 注意：这里需要 OrderService 提供 updateOrder 方法，或者直接注入 OrderMapper
                // 为了简化，假设你有一个方法能更新订单
                // orderService.updateOrderStatus(order.getId(), 2, 1);
            }
        }
        paymentRecord.setUpdateTime(LocalDateTime.now());
        paymentRecordMapper.updateById(paymentRecord);
        log.info("处理支付回调，支付单号：{}，第三方交易号：{}，状态：{}", payOrderNo, tradeNo, status);
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
        LambdaQueryWrapper<PaymentRecord> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(PaymentRecord::getUserId, userId);
        wrapper.eq(PaymentRecord::getDeleted, 0);
        wrapper.orderByDesc(PaymentRecord::getCreateTime);

        List<PaymentRecord> paymentRecords = paymentRecordMapper.selectList(wrapper);
        return paymentRecords.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    /**
     * 转换为响应对象
     */
    private PaymentResponse convertToResponse(PaymentRecord paymentRecord) {
        PaymentResponse response = new PaymentResponse();
        response.setId(paymentRecord.getId());
        response.setOrderId(paymentRecord.getOrderId());

        // ✅ 修正：将 Double 转换为 BigDecimal
        if (paymentRecord.getAmount() != null) {
            response.setAmount(java.math.BigDecimal.valueOf(paymentRecord.getAmount()));
        }

        response.setStatus(paymentRecord.getStatus());

        // ✅ 适配字段名
        response.setPaymentMethod(paymentRecord.getPaymentMethod());
        response.setPayOrderNo(paymentRecord.getPayOrderNo());
        response.setTradeNo(paymentRecord.getTransactionId());

        response.setPayTime(paymentRecord.getPayTime());
        response.setCreateTime(paymentRecord.getCreateTime());
        return response;
    }

    /**
     * 生成支付单号
     */
    private String generatePayOrderNo() {
        return "PAY" + System.currentTimeMillis() + (int) (Math.random() * 1000);
    }

}
