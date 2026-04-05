package com.petcompany.platform;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 宠伴服务平台应用启动类
 */
@SpringBootApplication
@MapperScan("com.petcompany.platform.modules.*.mapper")
public class PetCompanionApplication {

    public static void main(String[] args) {
        SpringApplication.run(PetCompanionApplication.class, args);
    }

}
