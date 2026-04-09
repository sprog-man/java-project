package com.petcompany.platform.modules.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 订单实体类
 */
@Data
@TableName("pet_order")
public class Order {

    /**
     * 订单ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 服务提供者ID
     */
    private Long providerId;

    /**
     * 宠物ID
     */
    private Long petId;

    /**
     * 服务类型ID
     */
    private Long serviceTypeId;

    /**
     * 服务开始时间
     */
    private LocalDateTime serviceStartTime;

    /**
     * 服务结束时间
     */
    private LocalDateTime serviceEndTime;

    /**
     * 服务地址
     */
    private String address;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 备注信息
     */
    private String notes;

    /**
     * 订单价格
     */
    private Double price;

    /**
     * 订单状态：1-待支付，2-待接单，3-服务中，4-待评价，5-已完成，6-已取消
     */
    private Integer status;

    /**
     * 支付状态：0-未支付，1-已支付，2-退款中，3-已退款
     */
    private Integer payStatus;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer deleted;

}
