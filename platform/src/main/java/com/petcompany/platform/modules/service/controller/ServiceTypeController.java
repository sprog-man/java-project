package com.petcompany.platform.modules.service.controller;

import com.petcompany.platform.common.result.Result;
import com.petcompany.platform.modules.service.entity.ServiceType;
import com.petcompany.platform.modules.service.service.ServiceTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import java.util.List;

/**
 * 服务类型控制器
 */
@RestController
@RequestMapping("/service-type")
@Slf4j
public class ServiceTypeController {

    @Resource
    private ServiceTypeService serviceTypeService;

    /**
     * 获取所有服务类型
     */
    @GetMapping("/list")
    public Result<List<ServiceType>> getAllServiceTypes() {
        log.info("获取所有服务类型");
        List<ServiceType> serviceTypes = serviceTypeService.getAllServiceTypes();
        return Result.success(serviceTypes);
    }

    /**
     * 根据ID获取服务类型
     */
    @GetMapping("/{id}")
    public Result<ServiceType> getServiceTypeById(@PathVariable Long id) {
        log.info("获取服务类型，ID：{}", id);
        ServiceType serviceType = serviceTypeService.getServiceTypeById(id);
        if (serviceType == null) {
            return Result.fail(com.petcompany.platform.common.result.ResultCode.NOT_FOUND);
        }
        return Result.success(serviceType);
    }

}
