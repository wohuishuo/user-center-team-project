package com.usercenter.exception;

import com.usercenter.common.ErrorCode;

/**
 * 自定义业务异常:业务校验失败时抛出,由全局异常处理器统一转成标准返回。
 * 对应网页书:实战篇 / 后端架构(公共件二:全局异常处理)。
 */
public class BusinessException extends RuntimeException {

    private final int code;

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.code = errorCode.getCode();
    }

    public int getCode() {
        return code;
    }
}
