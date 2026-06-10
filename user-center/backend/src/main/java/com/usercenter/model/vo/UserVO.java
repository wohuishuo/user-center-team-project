package com.usercenter.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 脱敏后的用户视图对象:返回给前端,绝不含 userPassword。
 * 对应网页书:实战篇 / 认证模块(用户信息脱敏)。
 */
@Data
public class UserVO implements Serializable {
    private Long id;
    private String userAccount;
    private String username;
    private String avatarUrl;
    private Integer gender;
    private String phone;
    private String email;
    private String planetCode;
    private Integer userRole;
    private Integer userStatus;
    private Date createTime;
}
