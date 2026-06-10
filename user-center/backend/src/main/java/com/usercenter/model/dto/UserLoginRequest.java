package com.usercenter.model.dto;

import lombok.Data;

import java.io.Serializable;

/** 登录请求体。 */
@Data
public class UserLoginRequest implements Serializable {
    private String userAccount;
    private String userPassword;
}
