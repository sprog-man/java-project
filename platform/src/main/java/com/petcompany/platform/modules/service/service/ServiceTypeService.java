package com.petcompany.platform.modules.service.service;

import com.petcompany.platform.modules.service.entity.ServiceType;

import java.util.List;

/**
 * 服务类型服务接口
 */
public interface ServiceTypeService {

    /**
     * 根据ID获取服务类型
     */
    ServiceType getServiceTypeById(Long id);

    /**
     * 获取所有服务类型
     */
    List<ServiceType> getAllServiceTypes();

}
