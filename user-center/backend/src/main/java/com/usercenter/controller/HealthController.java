package com.usercenter.controller;

import com.usercenter.common.BaseResponse;
import com.usercenter.common.ResultUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 健康检查 / 骨架验证接口。
 */
@Tag(name = "系统", description = "健康检查等")
@RestController
public class HealthController {

    @Operation(summary = "健康检查")
    @GetMapping("/health")
    public BaseResponse<String> health() {
        return ResultUtils.success("user-center is up");
    }
}
