package com.petcompany.platform.modules.pet.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 宠物更新请求DTO
 */
@Data
public class PetUpdateRequest {

    /**
     * 宠物ID
     */
    @NotNull(message = "宠物ID不能为空")
    private Long id;

    /**
     * 宠物名称
     */
    @NotBlank(message = "宠物名称不能为空")
    private String name;

    /**
     * 宠物类型：1-猫，2-狗，3-其他
     */
    @NotNull(message = "宠物类型不能为空")
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

}
