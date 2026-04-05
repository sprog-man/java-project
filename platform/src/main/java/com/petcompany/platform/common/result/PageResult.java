package com.petcompany.platform.common.result;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页结果类
 */
@Data
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 数据列表
     */
    private List<T> records;

    /**
     * 总条数
     */
    private long total;

    /**
     * 每页条数
     */
    private long size;

    /**
     * 当前页码
     */
    private long current;

    /**
     * 总页数
     */
    private long pages;

    /**
     * 构造方法
     */
    public PageResult(List<T> records, long total, long size, long current) {
        this.records = records;
        this.total = total;
        this.size = size;
        this.current = current;
        this.pages = total % size == 0 ? total / size : total / size + 1;
    }

    /**
     * 构造方法
     */
    public PageResult(List<T> records, long total) {
        this.records = records;
        this.total = total;
        this.size = total;
        this.current = 1;
        this.pages = 1;
    }

    /**
     * 空结果
     */
    public static <T> PageResult<T> empty() {
        return new PageResult<>(null, 0, 0, 0);
    }

}
