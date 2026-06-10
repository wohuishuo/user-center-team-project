package com.usercenter.controller;

import com.usercenter.common.BaseResponse;
import com.usercenter.common.ResultUtils;
import com.usercenter.common.SecurityUtils;
import com.usercenter.exception.BusinessException;
import com.usercenter.common.ErrorCode;
import com.usercenter.model.dto.UserLoginRequest;
import com.usercenter.model.dto.UserRegisterRequest;
import com.usercenter.model.entity.User;
import com.usercenter.model.vo.LoginUserVO;
import com.usercenter.model.vo.UserVO;
import com.usercenter.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

/**
 * 用户接口。Controller 只做请求接收与基本判空,业务校验在 Service。
 * 对应网页书:实战篇 / 认证模块、后端架构。
 */
@Tag(name = "用户", description = "注册 / 登录 / 当前用户")
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "注册")
    @PostMapping("/register")
    public BaseResponse<Long> register(@RequestBody UserRegisterRequest req) {
        if (req == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long id = userService.userRegister(
                req.getUserAccount(), req.getUserPassword(), req.getCheckPassword(), req.getPlanetCode());
        return ResultUtils.success(id);
    }

    @Operation(summary = "登录")
    @PostMapping("/login")
    public BaseResponse<LoginUserVO> login(@RequestBody UserLoginRequest req) {
        if (req == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return ResultUtils.success(userService.userLogin(req.getUserAccount(), req.getUserPassword()));
    }

    @Operation(summary = "获取当前登录用户")
    @GetMapping("/current")
    public BaseResponse<UserVO> current() {
        Long userId = SecurityUtils.getCurrentUserId();
        User user = userService.getById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        return ResultUtils.success(userService.toUserVO(user));
    }
}
