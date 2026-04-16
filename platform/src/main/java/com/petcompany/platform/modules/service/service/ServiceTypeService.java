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


    /**
     * 暴露接口给其他服务调用,统计服务类型数量
     */
    long countAllServiceTypes();

    // ✅ 新增：管理员添加服务类型
    void saveServiceType(ServiceType serviceType);

    // ✅ 新增：管理员更新服务类型
    void updateServiceType(ServiceType serviceType);

    // ✅ 新增：管理员删除服务类型（逻辑删除）
    void deleteServiceType(Long id);

}
