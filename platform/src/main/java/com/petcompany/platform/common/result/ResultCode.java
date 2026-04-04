package com.petcompany.platform.common.result;

/**
 * 响应状态码枚举
 */
public enum ResultCode {

    SUCCESS(200, "成功"),
    FAIL(500, "失败"),
    UNAUTHORIZED(401, "未授权"),
    FORBIDDEN(403, "禁止访问"),
    NOT_FOUND(404, "资源不存在"),
    PARAM_ERROR(400, "参数错误"),
    BUSINESS_ERROR(501, "业务错误"),
    SYSTEM_ERROR(502, "系统错误");

    private final int code;
    private final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
