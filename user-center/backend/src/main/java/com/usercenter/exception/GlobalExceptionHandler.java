package com.usercenter.exception;

import com.usercenter.common.BaseResponse;
import com.usercenter.common.ErrorCode;
import com.usercenter.common.ResultUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理:把业务异常和未捕获异常统一转成标准返回。
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BusinessException.class)
    public BaseResponse<?> handleBusinessException(BusinessException e) {
        log.warn("BusinessException: {}", e.getMessage());
        return ResultUtils.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public BaseResponse<?> handleException(Exception e) {
        log.error("System error", e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, ErrorCode.SYSTEM_ERROR.getMessage());
    }
}
