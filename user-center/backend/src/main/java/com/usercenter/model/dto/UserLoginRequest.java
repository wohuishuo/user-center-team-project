package com.usercenter.model.dto;

import lombok.Data;

import java.io.Serializable;

/** 登录请求体。 */
@Data
public class UserLoginRequest implements Serializable {
    private String userAccount;
    private String userPassword;
    /** 登录端类型:App / Web / Desktop。客户端不传则默认 Web(用于书域统计)。 */
    private String loginType;
}
