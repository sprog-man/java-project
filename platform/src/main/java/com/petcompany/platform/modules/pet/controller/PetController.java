package com.petcompany.platform.modules.pet.controller;

import com.petcompany.platform.common.result.Result;
import com.petcompany.platform.modules.pet.dto.PetCreateRequest;
import com.petcompany.platform.modules.pet.dto.PetResponse;
import com.petcompany.platform.modules.pet.dto.PetUpdateRequest;
import com.petcompany.platform.modules.pet.service.PetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import java.util.List;

/**
 * 宠物控制器
 */
@RestController
@RequestMapping("/pet")
@Slf4j
public class PetController {

    @Resource
    private PetService petService;

    /**
     * 创建宠物
     */
    @PostMapping("/add")
    public Result<?> createPet(@Valid @RequestBody PetCreateRequest request, Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        petService.createPet(userId, request);
        return Result.success("创建宠物成功");
    }

    /**
     * 更新宠物
     */
    @PutMapping("/update")
    public Result<?> updatePet(@Valid @RequestBody PetUpdateRequest request, Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        petService.updatePet(userId, request);
        return Result.success("更新宠物成功");
    }

    /**
     * 删除宠物
     */
    @DeleteMapping("/{id}")
    public Result<?> deletePet(@PathVariable Long id, Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        petService.deletePet(userId, id);
        return Result.success("删除宠物成功");
    }

    /**
     * 获取宠物列表
     */
    @GetMapping("/list")
    public Result<List<PetResponse>> getPetList(Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        List<PetResponse> pets = petService.getPetListByUserId(userId);
        return Result.success(pets);
    }

}
