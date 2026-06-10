package com.usercenter.model.dto;

import lombok.Data;

import java.io.Serializable;

/** 注册请求体。 */
@Data
public class UserRegisterRequest implements Serializable {
    private String userAccount;
    private String userPassword;
    private String checkPassword;
    private String planetCode;
}
