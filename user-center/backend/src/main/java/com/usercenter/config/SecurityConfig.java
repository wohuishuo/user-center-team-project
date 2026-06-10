package com.usercenter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * 安全配置。
 * 对应网页书:技术图鉴 / Spring Security、实战篇 / 认证模块。
 *
 * M3 骨架阶段:放行健康检查、注册/登录与 Swagger,其余暂不强制拦截;
 * 到 M4 接入 JWT 过滤器后再逐步收紧。
 */
@Configuration
public class SecurityConfig {

    /** 密码编码器:BCrypt(自带随机盐 + 慢哈希)。 */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 前后端分离、使用 JWT,关闭 CSRF
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/health",
                    "/user/register",
                    "/user/login",
                    "/swagger-ui.html",
                    "/swagger-ui/**",
                    "/v3/api-docs/**"
                ).permitAll()
                .anyRequest().permitAll() // M3 暂时放行,M4 收紧为 authenticated()
            );
        return http.build();
    }
}
