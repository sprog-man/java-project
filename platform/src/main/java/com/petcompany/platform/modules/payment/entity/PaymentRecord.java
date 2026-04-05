package com.petcompany.platform.modules.payment.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
     * 支付金额
     */
    private Double amount;

    /**
     * 支付状态：0-待支付，1-支付成功，2-支付失败
     */
    private Integer status;

    /**
     * 支付方式：1-微信支付，2-支付宝
     */
    private Integer payType;

    /**
     * 支付单号
     */
    private String payOrderNo;

    /**
     * 交易单号
     */
    private String tradeNo;

    /**
     * 支付时间
     */
    private LocalDateTime payTime;

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
