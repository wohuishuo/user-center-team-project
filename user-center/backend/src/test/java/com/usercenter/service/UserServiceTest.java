package com.usercenter.service;

import com.usercenter.exception.BusinessException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 认证模块单元测试:每条需求对应一条断言。
 * 对应网页书:理论篇 / 软件测试、实战篇 / 测试。
 */
@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void register_shouldReject_whenParamBlank() {                 // 需求:非空
        assertThrows(BusinessException.class, () ->
                userService.userRegister("", "12345678", "12345678", "10001"));
    }

    @Test
    void register_shouldReject_whenAccountTooShort() {            // 需求:账号 ≥4 位
        assertThrows(BusinessException.class, () ->
                userService.userRegister("ab", "12345678", "12345678", "10001"));
    }

    @Test
    void register_shouldReject_whenPasswordTooShort() {           // 需求:密码 ≥8 位
        assertThrows(BusinessException.class, () ->
                userService.userRegister("alice", "123", "123", "10001"));
    }

    @Test
    void register_shouldReject_whenAccountHasSpecialChar() {      // 需求:账号无特殊字符
        assertThrows(BusinessException.class, () ->
                userService.userRegister("ali ce!", "12345678", "12345678", "10001"));
    }

    @Test
    void register_shouldReject_whenPasswordsMismatch() {          // 需求:两次密码一致
        assertThrows(BusinessException.class, () ->
                userService.userRegister("alice", "12345678", "87654321", "10001"));
    }

    @Test
    void bcrypt_shouldVerify_sameRawPassword() {                  // BCrypt:同一密码可校验通过
        String hash = passwordEncoder.encode("12345678");
        assertNotEquals("12345678", hash);                       // 不是明文
        assertTrue(passwordEncoder.matches("12345678", hash));   // 能比对成功
        assertFalse(passwordEncoder.matches("wrongpass", hash)); // 错密码失败
    }
}
