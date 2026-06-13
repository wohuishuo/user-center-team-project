package com.usercenter.controller;

import com.usercenter.common.BaseResponse;
import com.usercenter.common.ResultUtils;
import com.usercenter.common.SecurityUtils;
import com.usercenter.exception.BusinessException;
import com.usercenter.common.ErrorCode;
import com.usercenter.model.dto.UserLoginRequest;
import com.usercenter.model.dto.UserRegisterRequest;
import com.usercenter.model.vo.LoginUserVO;
import com.usercenter.model.vo.PageResult;
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

    @Operation(summary = "登录(成功后异步发布 UserLogin 事件)")
    @PostMapping("/login")
    public BaseResponse<LoginUserVO> login(@RequestBody UserLoginRequest req,
                                           jakarta.servlet.http.HttpServletRequest httpReq) {
        if (req == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return ResultUtils.success(userService.userLogin(
                req.getUserAccount(), req.getUserPassword(), req.getLoginType(), httpReq.getRemoteAddr()));
    }

    @Operation(summary = "获取当前登录用户")
    @GetMapping("/current")
    public BaseResponse<UserVO> current() {
        Long userId = SecurityUtils.getCurrentUserId();
        UserVO vo = userService.getCachedUserVO(userId); // 走 Redis 缓存
        if (vo == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        return ResultUtils.success(vo);
    }

    @Operation(summary = "按用户名分页查询用户(仅管理员)")
    @GetMapping("/search")
    public BaseResponse<PageResult<UserVO>> search(
            @RequestParam(required = false) String username,
            @RequestParam(defaultValue = "1") long current,
            @RequestParam(defaultValue = "10") long pageSize) {
        ensureAdmin();
        return ResultUtils.success(userService.searchUsers(username, current, pageSize));
    }

    @Operation(summary = "删除用户(仅管理员,逻辑删除)")
    @PostMapping("/delete")
    public BaseResponse<Boolean> delete(@RequestBody Long id) {
        ensureAdmin();
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return ResultUtils.success(userService.removeUser(id));
    }

    /** 管理操作前的鉴权:非管理员直接拒绝。 */
    private void ensureAdmin() {
        SecurityUtils.getCurrentUserId();           // 未登录抛 NOT_LOGIN
        if (!SecurityUtils.isAdmin()) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
    }
}
