package com.usercenter.common;

/**
 * 用户相关常量。把魔法数字提成有名字的常量(对应网页书:理论篇 / 软件实现)。
 */
public final class UserConstant {

    private UserConstant() {
    }

    /** 普通用户 */
    public static final int DEFAULT_ROLE = 0;

    /** 管理员 */
    public static final int ADMIN_ROLE = 1;
}
