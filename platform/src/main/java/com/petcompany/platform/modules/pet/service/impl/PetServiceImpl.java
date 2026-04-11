package com.petcompany.platform.modules.pet.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.petcompany.platform.common.exception.BusinessException;
import com.petcompany.platform.modules.pet.dto.PetCreateRequest;
import com.petcompany.platform.modules.pet.dto.PetResponse;
import com.petcompany.platform.modules.pet.dto.PetUpdateRequest;
import com.petcompany.platform.modules.pet.entity.Pet;
import com.petcompany.platform.modules.pet.service.PetService;
import com.petcompany.platform.modules.pet.mapper.PetMapper;
import com.petcompany.platform.modules.user.entity.User;
import com.petcompany.platform.modules.user.service.UserService;
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

    @Resource
    private UserService userService;

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

    /*
    * 管理员端分页查询宠物
    * */
    @Override
    public Page<PetResponse> getAdminPetPage(int page, int size, String keyword) {
        // 1. 创建分页对象
        Page<Pet> petPage = new Page<>(page, size);

        //2.构建查询条件
        LambdaQueryWrapper<Pet> wrapper = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank( keyword)){
            // 简单起见，先按宠物名称模糊搜索
            // 如果要按主人名称搜索，通常需要连表查询或先查用户ID
            wrapper.like(Pet::getName, keyword);
        }
        wrapper.eq(Pet::getDeleted, 0);

        // 3. 执行分页查询
        IPage<Pet> result = petMapper.selectPage(petPage, wrapper);

        //4.转换为DTO并填充主人信息
        Page<PetResponse> responsePage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
        List<PetResponse> responseList = result.getRecords().stream().map(pet -> {
            PetResponse response = convertToResponse(pet);
            // ✅ 关联查询主人名称
            if (pet.getUserId() != null) {
                User user = userService.getUserById(pet.getUserId());
                if (user != null) {
                    response.setOwnerName(user.getUsername() != null ? user.getUsername() : user.getUsername());
                }
            }
            return response;
        }).collect(Collectors.toList());
        responsePage.setRecords(responseList);
        return responsePage;
    }

}
