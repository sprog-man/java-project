package com.petcompany.platform.modules.common.controller;

import com.petcompany.platform.common.result.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 健康检查控制器
 */
@RestController
@RequestMapping("/health")
public class HealthController {

    /**
     * 健康检查
     */
    @GetMapping
    public Result<?> health() {
        return Result.success("服务运行正常");
    }

}
