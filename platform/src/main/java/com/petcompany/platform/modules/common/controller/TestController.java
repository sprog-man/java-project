package com.petcompany.platform.modules.common.controller;

import com.petcompany.platform.common.result.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试控制器
 */
@RestController
@RequestMapping("/test")
public class TestController {

    /**
     * 测试接口
     */
    @GetMapping
    public Result<?> test() {
        return Result.success("测试接口响应正常");
    }

}
