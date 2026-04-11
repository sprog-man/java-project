package com.petcompany.platform.modules.service.controller;

import com.petcompany.platform.common.result.Result;
import com.petcompany.platform.modules.service.entity.ServiceProvider;
import com.petcompany.platform.modules.service.entity.ServiceType;
import com.petcompany.platform.modules.service.service.ServiceProviderService;
import com.petcompany.platform.modules.service.service.ServiceTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/admin/services") // ✅ 统一的管理员服务管理入口
@Slf4j
public class AdminServiceController {

    @Resource
    private ServiceTypeService serviceTypeService;

    @Resource
    private ServiceProviderService serviceProviderService; // ✅ 注入 ServiceProviderService



    /**
     * 管理员获取待审核的服务者申请列表
     */
    @GetMapping("/provider/pending")
    public Result<List<ServiceProvider>> getPendingProviders() {
        log.info("管理员请求获取待审核服务者列表");
        // ✅ 直接调用接口方法，不要强转
        List<ServiceProvider> list = serviceProviderService.getPendingApplications();
        return Result.success(list);
    }
    /**
     * 管理员审核服务提供者申请
     * @param providerId 服务提供者申请ID
     * @param status 审核结果：1-通过，2-拒绝
     */
    @PostMapping("/provider/audit/{providerId}")
    public Result<?> auditServiceProvider(@PathVariable Long providerId, @RequestParam Integer status) {
        log.info("管理员审核服务者: providerId={}, status={}", providerId, status);
        serviceProviderService.auditApplication(providerId, status);
        return Result.success("审核操作成功");
    }

    /**
     * 管理员获取所有服务类型列表
     */
    @GetMapping
    public Result<List<ServiceType>> getAllServices() {
        log.info("管理员请求获取服务类型列表");
        List<ServiceType> list = serviceTypeService.getAllServiceTypes();
        return Result.success(list);
    }

    /**
     * 管理员添加服务类型
     */
    @PostMapping
    public Result<?> addService(@RequestBody ServiceType serviceType) {
        log.info("管理员添加服务: {}", serviceType.getName());
        serviceTypeService.saveServiceType(serviceType);
        return Result.success("添加成功");
    }

    /**
     * 管理员更新服务类型
     */
    @PutMapping("/{id}")
    public Result<?> updateService(@PathVariable Long id, @RequestBody ServiceType serviceType) {
        try {
            log.info("管理员更新服务: ID={}, 数据={}", id, serviceType);
            serviceType.setId(id);
            serviceTypeService.updateServiceType(serviceType);
            return Result.success("更新成功");
        } catch (Exception e) {
            log.error("更新服务失败", e);
            return Result.fail("更新失败: " + e.getMessage()); // ✅ 这样前端就能看到具体报错了
        }
    }

    /**
     * 管理员删除服务类型（逻辑删除）
     */
    @DeleteMapping("/{id}")
    public Result<?> deleteService(@PathVariable Long id) {
        log.info("管理员删除服务: ID={}", id);
        serviceTypeService.deleteServiceType(id);
        return Result.success("删除成功");
    }
}