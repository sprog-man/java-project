package com.petcompany.platform.modules.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.petcompany.platform.modules.service.entity.ServiceType;
import org.apache.ibatis.annotations.Mapper;

/**
 * 服务类型Mapper接口
 */
@Mapper
public interface ServiceTypeMapper extends BaseMapper<ServiceType> {

}
