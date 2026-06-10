package com.usercenter.config;

import com.usercenter.common.UserConstant;
import com.usercenter.util.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * JWT 认证过滤器:每个请求先过它,解析 Authorization 头里的令牌,
 * 解出用户身份放进 SecurityContext。
 * 对应网页书:实战篇 / 认证模块、技术图鉴 / Spring Security。
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    public JwtAuthenticationFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
                Claims claims = jwtUtils.parse(token);
                Long userId = Long.valueOf(claims.getSubject());
                Integer role = claims.get("role", Integer.class);
                String authority = (role != null && role == UserConstant.ADMIN_ROLE) ? "ROLE_ADMIN" : "ROLE_USER";
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        userId, null, List.of(new SimpleGrantedAuthority(authority)));
                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (Exception e) {
                // 令牌无效:不设置认证,后续按未登录处理
                SecurityContextHolder.clearContext();
            }
        }
        chain.doFilter(request, response);
    }
}
