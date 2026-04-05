package com.petcompany.platform.common.result;

import lombok.Data;

/**
 * 分页请求类
 */
@Data
public class PageRequest {

    /**
     * 当前页码
     */
    private Integer page = 1;

    /**
     * 每页大小
     */
    private Integer size = 10;

}
