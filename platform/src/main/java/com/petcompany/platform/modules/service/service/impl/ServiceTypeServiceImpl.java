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
import java.util.List;
import java.util.concurrent.TimeUnit;

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
        String key = String.format(SERVICE_TYPE_KEY, id);
        ServiceType serviceType = redisCacheUtil.getCache(key, ServiceType.class);
        if (serviceType != null) {
            log.debug("从缓存获取服务类型，ID：{}", id);
            return serviceType;
        }

        // 从数据库获取
        serviceType = serviceTypeMapper.selectById(id);

        // 存入缓存，有效期1小时
        if (serviceType != null) {
            redisCacheUtil.setCache(key, serviceType, 1, TimeUnit.HOURS);
            log.debug("服务类型缓存成功，ID：{}", id);
        }

        return serviceType;
    }

    @Override
    public List<ServiceType> getAllServiceTypes() {
        // 从缓存获取
        List<ServiceType> serviceTypes = redisCacheUtil.getCache(SERVICE_TYPE_LIST_KEY, List.class);
        if (serviceTypes != null) {
            log.debug("从缓存获取服务类型列表");
            return serviceTypes;
        }

        // 从数据库获取
        LambdaQueryWrapper<ServiceType> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ServiceType::getDeleted, 0);
        serviceTypes = serviceTypeMapper.selectList(wrapper);

        // 存入缓存，有效期1小时
        redisCacheUtil.setCache(SERVICE_TYPE_LIST_KEY, serviceTypes, 1, TimeUnit.HOURS);
        log.debug("服务类型列表缓存成功");

        return serviceTypes;
    }

}
