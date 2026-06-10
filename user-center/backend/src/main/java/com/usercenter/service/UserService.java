package com.usercenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.usercenter.model.entity.User;
import com.usercenter.model.vo.LoginUserVO;
import com.usercenter.model.vo.PageResult;
import com.usercenter.model.vo.UserVO;

/**
 * 用户服务。业务校验放在这一层(对应网页书:实战篇 / 后端架构)。
 */
public interface UserService extends IService<User> {

    /** 注册,返回新用户 id。 */
    long userRegister(String userAccount, String userPassword, String checkPassword, String planetCode);

    /** 登录,返回 JWT + 脱敏用户。 */
    LoginUserVO userLogin(String userAccount, String userPassword);

    /** 脱敏:User -> UserVO。 */
    UserVO toUserVO(User user);

    /** 按用户名模糊分页查询(管理员)。 */
    PageResult<UserVO> searchUsers(String username, long current, long pageSize);

    /** 逻辑删除用户(管理员)。 */
    boolean removeUser(long id);

    /** 按 id 取脱敏用户,结果走 Redis 缓存。 */
    UserVO getCachedUserVO(long id);
}
