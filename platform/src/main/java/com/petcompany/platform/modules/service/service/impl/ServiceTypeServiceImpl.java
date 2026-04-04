package com.petcompany.platform.modules.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.petcompany.platform.modules.service.entity.ServiceType;
import com.petcompany.platform.modules.service.service.ServiceTypeService;
import com.petcompany.platform.modules.service.mapper.ServiceTypeMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.List;

/**
 * 服务类型服务实现类
 */
@Service
@Slf4j
public class ServiceTypeServiceImpl implements ServiceTypeService {

    @Resource
    private ServiceTypeMapper serviceTypeMapper;

    @Override
    public ServiceType getServiceTypeById(Long id) {
        return serviceTypeMapper.selectById(id);
    }

    @Override
    public List<ServiceType> getAllServiceTypes() {
        LambdaQueryWrapper<ServiceType> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ServiceType::getDeleted, 0);
        return serviceTypeMapper.selectList(wrapper);
    }

}
