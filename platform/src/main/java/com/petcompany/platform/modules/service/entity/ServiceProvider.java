package com.petcompany.platform.modules.service.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 服务提供者实体
 */
@Data
@TableName("service_provider")
public class ServiceProvider {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 身份证照片
     */
    private String idCardPhoto;

    /**
     * 服务经验描述
     */
    private String experience;

    /**
     * 审核状态：0-待审核，1-审核通过，2-审核拒绝
     */
    @TableField("verify_status") // ✅ 显式映射
    private Integer verifyStatus;

    /**
     * 服务半径（公里）
     */
    private Integer serviceRadius;

    /**
     * 纬度
     */
    private Double latitude;

    /**
     * 经度
     */
    private Double longitude;

    /**
     * 平均评分
     */
    private Double rating;

    /**
     * 评价数量
     */
    private Integer reviewCount;


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
