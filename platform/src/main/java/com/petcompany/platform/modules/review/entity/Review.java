package com.petcompany.platform.modules.review.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 评价实体
 */
@Data
@TableName("review")
public class Review {

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
     * 被评价用户ID（对应数据库 target_id）
     */
    @TableField("target_id") // ✅ 显式映射
    private Long targetId;

    /**
     * 服务提供者ID
     */
    private Long providerId;


    /**
     * 评分：1-5星（对应数据库 rating）
     */
    @TableField("rating") // ✅ 显式映射
    private Integer rating;

    /**
     * 评价内容
     */
    private String content;

    /**
     * 评价图片（多个图片URL用逗号分隔）
     */
    private String images;



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
