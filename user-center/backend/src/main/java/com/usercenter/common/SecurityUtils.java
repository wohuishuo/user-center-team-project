package com.usercenter.common;

import com.usercenter.exception.BusinessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 从 SecurityContext 取当前登录用户信息的小工具。
 */
public final class SecurityUtils {

    private SecurityUtils() {
    }

    /** 取当前登录用户 id,未登录则抛 NOT_LOGIN。 */
    public static Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof Long)) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        return (Long) auth.getPrincipal();
    }

    /** 当前用户是否为管理员。 */
    public static boolean isAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));
    }
}
