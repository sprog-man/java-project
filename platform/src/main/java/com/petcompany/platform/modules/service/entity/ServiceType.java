package com.petcompany.platform.modules.service.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 服务类型实体类
 */
@Data
@TableName("service_type")
public class ServiceType {

    /**
     * 服务类型ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 服务名称
     */
    private String name;

    /**
     * 服务描述
     */
    private String description;

    /**
     * 基础价格
     */
    private Double basePrice;

    /**
     * 每小时价格
     */
    private Double pricePerHour;

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
