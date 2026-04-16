package com.petcompany.platform.modules.pet.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 宠物实体类
 */
@Data
@TableName("pet")
public class Pet {

    /**
     * 宠物ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 宠物名称
     */
    private String name;

    /**
     * 宠物类型：1-猫，2-狗，3-其他
     */
    private Integer type;

    /**
     * 宠物品种
     */
    private String breed;

    /**
     * 宠物年龄
     */
    private Integer age;

    /**
     * 宠物性别：1-公，2-母
     */
    private Integer gender;

    /**
     * 宠物体重
     */
    private Double weight;

    /**
     * 宠物照片
     */
    private String photo;

    /**
     * 宠物描述
     */
    private String description;

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
