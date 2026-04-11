package com.petcompany.platform.modules.pet.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 宠物响应DTO
 */
@Data
public class PetResponse {

    /**
     * 宠物ID
     */
    private Long id;

    /**
     * 宠物名称
     */
    private String name;

    /**
     * 宠物类型：1-猫，2-狗，3-其他
     */
    private Integer type;

    /**
     * 宠物类型名称
     */
    private String typeName;

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
     * 宠物性别名称
     */
    private String genderName;

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
     * 主人名称（非数据库字段，用于展示）
     */
    private String ownerName;

}
