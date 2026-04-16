package com.petcompany.platform.modules.payment.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 支付记录实体
 */
@Data
@TableName("payment_record")
public class PaymentRecord {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 支付金额
     */
    private Double amount;

    /**
     * 支付方式（对应数据库 payment_method）
     */
    @TableField("payment_method") // ✅ 显式映射
    private String paymentMethod;

    /**
     * 内部支付单号（用于关联业务）
     */
    @TableField("pay_order_no")
    private String payOrderNo;

    /**
     * 交易号（对应数据库 transaction_id）
     */
    @TableField("transaction_id") // ✅ 显式映射
    private String transactionId;

    /**
     * 支付时间
     */
    private LocalDateTime payTime;


    /**
     * 支付状态：0-待支付，1-支付成功，2-支付失败
     */
    private Integer status;


    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 逻辑删除标记
     */
    private Integer deleted;

}
