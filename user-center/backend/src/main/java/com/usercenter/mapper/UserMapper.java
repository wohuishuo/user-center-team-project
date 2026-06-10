package com.usercenter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.usercenter.model.entity.User;

/**
 * 用户表 Mapper:继承 BaseMapper 即自动拥有 CRUD。
 * 对应网页书:技术图鉴 / MyBatis-Plus。
 */
public interface UserMapper extends BaseMapper<User> {
}
