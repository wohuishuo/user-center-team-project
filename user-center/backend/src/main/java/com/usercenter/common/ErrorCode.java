package com.usercenter.common;

/**
 * 业务错误码。区别于 HTTP 状态码——这里描述的是“业务层面对不对”。
 * 对应网页书:实战篇 / 后端架构。
 */
public enum ErrorCode {

    SUCCESS(0, "ok"),
    PARAMS_ERROR(40000, "请求参数错误"),
    NOT_LOGIN(40100, "未登录"),
    NO_AUTH(40101, "无权限"),
    NOT_FOUND(40400, "请求数据不存在"),
    SYSTEM_ERROR(50000, "系统内部异常");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
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
