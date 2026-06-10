package com.usercenter.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户实体,对应 user 表。
 * 对应网页书:实战篇 / 数据库设计。
 */
@Data
@TableName("user")
public class User implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String userAccount;

    private String userPassword;

    private String username;

    private String avatarUrl;

    private Integer gender;

    private String phone;

    private String email;

    private String planetCode;

    private Integer userRole;

    private Integer userStatus;

    private Date createTime;

    private Date updateTime;

    /** 逻辑删除:0 有效 / 1 已删,MyBatis-Plus 自动过滤。 */
    @TableLogic
    private Integer isDelete;
}
