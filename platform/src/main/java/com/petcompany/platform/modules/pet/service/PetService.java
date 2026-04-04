package com.petcompany.platform.modules.pet.service;

import com.petcompany.platform.modules.pet.dto.PetCreateRequest;
import com.petcompany.platform.modules.pet.dto.PetResponse;
import com.petcompany.platform.modules.pet.dto.PetUpdateRequest;
import com.petcompany.platform.modules.pet.entity.Pet;

import java.util.List;

/**
 * 宠物服务接口
 */
public interface PetService {

    /**
     * 创建宠物
     */
    Pet createPet(Long userId, PetCreateRequest request);

    /**
     * 更新宠物
     */
    Pet updatePet(Long userId, PetUpdateRequest request);

    /**
     * 删除宠物
     */
    void deletePet(Long userId, Long petId);

    /**
     * 根据ID获取宠物
     */
    Pet getPetById(Long petId);

    /**
     * 获取用户的宠物列表
     */
    List<PetResponse> getPetListByUserId(Long userId);

}
