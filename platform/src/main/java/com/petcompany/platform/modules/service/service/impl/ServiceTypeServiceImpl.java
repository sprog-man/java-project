package com.petcompany.platform.modules.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.petcompany.platform.common.utils.RedisCacheUtil;
import com.petcompany.platform.modules.service.entity.ServiceType;
import com.petcompany.platform.modules.service.service.ServiceTypeService;
import com.petcompany.platform.modules.service.mapper.ServiceTypeMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;

import java.time.LocalDateTime;
import java.util.List;


/**
 * 服务类型服务实现类
 */
@Service
@Slf4j
public class ServiceTypeServiceImpl implements ServiceTypeService {

    @Resource
    private ServiceTypeMapper serviceTypeMapper;

    @Resource
    private RedisCacheUtil redisCacheUtil;

    private static final String SERVICE_TYPE_LIST_KEY = "service:type:list";
    private static final String SERVICE_TYPE_KEY = "service:type:%d";

    @Override
    public ServiceType getServiceTypeById(Long id) {
        // 从缓存获取
        /*String key = String.format(SERVICE_TYPE_KEY, id);
        ServiceType serviceType = redisCacheUtil.getCache(key, ServiceType.class);
        if (serviceType != null) {
            log.debug("从缓存获取服务类型，ID：{}", id);
            return serviceType;
        }*/

        // 从数据库获取
        ServiceType serviceType = serviceTypeMapper.selectById(id);

        // 存入缓存，有效期1小时
        /*if (serviceType != null) {
            redisCacheUtil.setCache(key, serviceType, 1, TimeUnit.HOURS);
            log.debug("服务类型缓存成功，ID：{}", id);
        }*/

        return serviceType;
    }

    @Override
    public List<ServiceType> getAllServiceTypes() {
        // ✅ 临时注释掉 Redis 逻辑，直接查库，排查是否是缓存导致的序列化错误
        /*
        List<ServiceType> serviceTypes = redisCacheUtil.getCache(
                SERVICE_TYPE_LIST_KEY,
                new TypeReference<List<ServiceType>>() {}
        );

        if (serviceTypes != null && !serviceTypes.isEmpty()) {
            log.debug("从缓存获取服务类型列表，数量：{}", serviceTypes.size());
            return serviceTypes;
        }
        */

        // 从数据库获取
        LambdaQueryWrapper<ServiceType> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ServiceType::getDeleted, 0);
        List<ServiceType> serviceTypes = serviceTypeMapper.selectList(wrapper);

        if (serviceTypes == null) {
            serviceTypes = List.of();
        }

        // ✅ 临时注释掉存入缓存，防止写入坏数据
        // redisCacheUtil.setCache(SERVICE_TYPE_LIST_KEY, serviceTypes, 1, TimeUnit.HOURS);
        log.info("✅ 成功从数据库加载服务类型列表，数量：{}", serviceTypes.size());

        return serviceTypes;
    }

    /*
    * 统计服务类型总数
    * */
    @Override
    public long countAllServiceTypes() {
        LambdaQueryWrapper<ServiceType> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ServiceType::getDeleted, 0);
        return serviceTypeMapper.selectCount(wrapper);
    }


    @Override
    public void saveServiceType(ServiceType serviceType) {
        serviceType.setCreateTime(LocalDateTime.now());
        serviceType.setUpdateTime(LocalDateTime.now());
        serviceType.setDeleted(0);
        serviceTypeMapper.insert(serviceType);
        // ✅ 关键：写操作后必须清除列表缓存，否则前端看到的还是旧数据
        redisCacheUtil.deleteCache(SERVICE_TYPE_LIST_KEY);
    }

    @Override
    public void updateServiceType(ServiceType serviceType) {
        serviceType.setUpdateTime(LocalDateTime.now());
        serviceTypeMapper.updateById(serviceType);
        // ✅ 清除缓存
        redisCacheUtil.deleteCache(SERVICE_TYPE_LIST_KEY);
        redisCacheUtil.deleteCache(String.format(SERVICE_TYPE_KEY, serviceType.getId()));
    }

    @Override
    public void deleteServiceType(Long id) {
        // ✅ 改进：直接使用 MP 的 deleteById
        // 在配置了 logic-delete 后，这行代码会自动执行：
        // UPDATE service_type SET deleted=1 WHERE id=? AND deleted=0
        int rows = serviceTypeMapper.deleteById(id);

        if (rows > 0) {
            log.info("服务类型逻辑删除成功，ID：{}", id);
            // ✅ 清除缓存，确保前端看到的是最新列表
            redisCacheUtil.deleteCache(SERVICE_TYPE_LIST_KEY);
            redisCacheUtil.deleteCache(String.format(SERVICE_TYPE_KEY, id));
        } else {
            log.warn("服务类型删除失败（记录不存在或已被删除），ID：{}", id);
        }
    }

}
