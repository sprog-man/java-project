package com.petcompany.platform.modules.user.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
* 管理员首页统计信息响应
* */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminStatsResponse {
    private Long userCount;
    private Long serviceCount;
    private Long orderCount;
    private Long reviewCount;
}
