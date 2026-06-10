package com.usercenter.model.vo;

import lombok.Data;

import java.io.Serializable;

/** 登录返回:JWT 令牌 + 脱敏用户。 */
@Data
public class LoginUserVO implements Serializable {
    private String token;
    private UserVO user;

    public LoginUserVO(String token, UserVO user) {
        this.token = token;
        this.user = user;
    }
}
