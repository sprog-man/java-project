package com.petcompany.platform.common.exception;

import com.petcompany.platform.common.result.ResultCode;

/**
 * 业务异常类
 */
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 状态码
     */
    private int code;

    /**
     * 构造方法
     */
    public BusinessException() {
        super();
    }

    /**
     * 构造方法
     */
    public BusinessException(String message) {
        super(message);
        this.code = ResultCode.FAIL.getCode();
    }

    /**
     * 构造方法
     */
    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * 构造方法
     */
    public BusinessException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.code = resultCode.getCode();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

}
