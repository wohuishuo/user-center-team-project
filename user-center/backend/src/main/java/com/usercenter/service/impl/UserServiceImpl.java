package com.usercenter.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.usercenter.common.ErrorCode;
import com.usercenter.common.UserConstant;
import com.usercenter.exception.BusinessException;
import com.usercenter.mapper.UserMapper;
import com.usercenter.model.entity.User;
import com.usercenter.model.vo.LoginUserVO;
import com.usercenter.model.vo.UserVO;
import com.usercenter.service.UserService;
import com.usercenter.util.JwtUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

/**
 * 用户服务实现。
 * 对应网页书:实战篇 / 认证模块(BCrypt 存密码、JWT 登录态、脱敏)。
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    /** 账号只允许字母、数字、下划线。 */
    private static final Pattern ACCOUNT_PATTERN = Pattern.compile("^[a-zA-Z0-9_]+$");

    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public UserServiceImpl(PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword, String planetCode) {
        // 1. 参数校验(格式)
        if (isBlank(userAccount) || isBlank(userPassword) || isBlank(checkPassword) || isBlank(planetCode)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号长度不能小于 4 位");
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码长度不能小于 8 位");
        }
        if (!ACCOUNT_PATTERN.matcher(userAccount).matches()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号不能包含特殊字符");
        }
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入的密码不一致");
        }
        // 2. 唯一性校验
        if (this.lambdaQuery().eq(User::getUserAccount, userAccount).count() > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号已存在");
        }
        if (this.lambdaQuery().eq(User::getPlanetCode, planetCode).count() > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "星球编号已存在");
        }
        // 3. BCrypt 加密
        String encrypted = passwordEncoder.encode(userPassword);
        // 4. 入库
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encrypted);
        user.setPlanetCode(planetCode);
        user.setUserRole(UserConstant.DEFAULT_ROLE);
        user.setUserStatus(0);
        boolean ok = this.save(user);
        if (!ok) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败");
        }
        return user.getId();
    }

    @Override
    public LoginUserVO userLogin(String userAccount, String userPassword) {
        // 1. 参数校验
        if (isBlank(userAccount) || isBlank(userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4 || userPassword.length() < 8
                || !ACCOUNT_PATTERN.matcher(userAccount).matches()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号或密码错误");
        }
        // 2. 查用户
        User user = this.lambdaQuery().eq(User::getUserAccount, userAccount).one();
        // 3. BCrypt 比对(账号不存在与密码错误统一提示,不泄露账号是否存在)
        if (user == null || !passwordEncoder.matches(userPassword, user.getUserPassword())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号或密码错误");
        }
        // 4. 签发 JWT + 返回脱敏用户
        String token = jwtUtils.createToken(user.getId(), user.getUserRole());
        return new LoginUserVO(token, toUserVO(user));
    }

    @Override
    public UserVO toUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(user, vo); // 不含 userPassword 字段,天然脱敏
        return vo;
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
