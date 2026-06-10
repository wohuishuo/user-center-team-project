package com.usercenter;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 用户中心后端启动类。
 * 对应网页书:技术图鉴 / Spring Boot。
 */
@SpringBootApplication
@MapperScan("com.usercenter.mapper")
public class UserCenterApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserCenterApplication.class, args);
    }
}
