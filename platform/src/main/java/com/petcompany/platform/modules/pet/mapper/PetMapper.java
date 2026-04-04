package com.petcompany.platform.modules.pet.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.petcompany.platform.modules.pet.entity.Pet;
import org.apache.ibatis.annotations.Mapper;

/**
 * 宠物Mapper接口
 */
@Mapper
public interface PetMapper extends BaseMapper<Pet> {

}
