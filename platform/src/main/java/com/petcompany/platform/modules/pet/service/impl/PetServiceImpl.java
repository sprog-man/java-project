package com.petcompany.platform.modules.pet.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.petcompany.platform.common.exception.BusinessException;
import com.petcompany.platform.modules.pet.dto.PetCreateRequest;
import com.petcompany.platform.modules.pet.dto.PetResponse;
import com.petcompany.platform.modules.pet.dto.PetUpdateRequest;
import com.petcompany.platform.modules.pet.entity.Pet;
import com.petcompany.platform.modules.pet.service.PetService;
import com.petcompany.platform.modules.pet.mapper.PetMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 宠物服务实现类
 */
@Service
@Slf4j
public class PetServiceImpl implements PetService {

    @Resource
    private PetMapper petMapper;

    @Override
    public Pet createPet(Long userId, PetCreateRequest request) {
        Pet pet = new Pet();
        BeanUtils.copyProperties(request, pet);
        pet.setUserId(userId);
        pet.setCreateTime(LocalDateTime.now());
        pet.setUpdateTime(LocalDateTime.now());
        pet.setDeleted(0);

        petMapper.insert(pet);
        return pet;
    }

    @Override
    public Pet updatePet(Long userId, PetUpdateRequest request) {
        // 检查宠物是否存在
        Pet pet = petMapper.selectById(request.getId());
        if (pet == null) {
            throw new BusinessException("宠物不存在");
        }

        // 检查是否是用户自己的宠物
        if (!pet.getUserId().equals(userId)) {
            throw new BusinessException("无权操作此宠物");
        }

        // 更新宠物信息
        BeanUtils.copyProperties(request, pet);
        pet.setUpdateTime(LocalDateTime.now());

        petMapper.updateById(pet);
        return pet;
    }

    @Override
    public void deletePet(Long userId, Long petId) {
        // 检查宠物是否存在
        Pet pet = petMapper.selectById(petId);
        if (pet == null) {
            throw new BusinessException("宠物不存在");
        }

        // 检查是否是用户自己的宠物
        if (!pet.getUserId().equals(userId)) {
            throw new BusinessException("无权操作此宠物");
        }

        // 逻辑删除宠物（MyBatis-Plus会自动转换为UPDATE语句）
        petMapper.deleteById(petId);
    }

    @Override
    public Pet getPetById(Long petId) {
        return petMapper.selectById(petId);
    }

    @Override
    public List<PetResponse> getPetListByUserId(Long userId) {
        LambdaQueryWrapper<Pet> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Pet::getUserId, userId);
        List<Pet> pets = petMapper.selectList(wrapper);

        // 转换为响应DTO
        return pets.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    /**
     * 转换为响应DTO
     */
    private PetResponse convertToResponse(Pet pet) {
        PetResponse response = new PetResponse();
        BeanUtils.copyProperties(pet, response);

        // 设置宠物类型名称
        if (pet.getType() != null) {
            switch (pet.getType()) {
                case 1: response.setTypeName("猫"); break;
                case 2: response.setTypeName("狗"); break;
                case 3: response.setTypeName("其他"); break;
                default: response.setTypeName("未知");
            }
        }

        // 设置宠物性别名称
        if (pet.getGender() != null) {
            switch (pet.getGender()) {
                case 1: response.setGenderName("公"); break;
                case 2: response.setGenderName("母"); break;
                default: response.setGenderName("未知");
            }
        }

        return response;
    }

}
