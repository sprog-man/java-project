package com.petcompany.platform.modules.pet.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.petcompany.platform.common.result.Result;
import com.petcompany.platform.modules.pet.dto.PetResponse;
import com.petcompany.platform.modules.pet.service.PetService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/pets")
@Slf4j
public class AdminPetController {
    @Resource
    private PetService petService;

    /*
    * 获取宠物列表(分页)
    * @param page页码
    * @param size 每页数量
    * @param keyword 搜索关键字(宠物名称或主人名称)
    * @return 分页列表
    */
    @GetMapping
    public Result<Page<PetResponse>> getPetList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer type // ✅ 新增：接收类型筛选
    ){
        log.info("管理员获取宠物列表：page={},size={},keyword={},type={}",page,size,keyword,type);
        Page<PetResponse> result = petService.getAdminPetPage(page,size,keyword,type);
        return Result.success("获取宠物列表成功",result);
    }

}
