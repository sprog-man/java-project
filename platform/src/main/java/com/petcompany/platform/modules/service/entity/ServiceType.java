package com.petcompany.platform.modules.service.entity;

import com.baomidou.mybatisplus.annotation.*;
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
    @TableField(value = "description", jdbcType = org.apache.ibatis.type.JdbcType.LONGVARCHAR)
    private String description;

    /**
     * 服务价格 (每小时)
     */
    @TableField("price")
    private Double price;


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
