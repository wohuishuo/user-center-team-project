package com.usercenter.event;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * UserLogin 领域事件(契约与书域平台 P5 事件表一致,勿改字段名):
 * userId / loginType(App|Web|Desktop)/ loginTime / ipAddress。
 * 经 RabbitMQ fanout exchange "user.events" 广播,由统计服务消费。
 */
public record UserLoginEvent(
        Long userId,
        String loginType,
        LocalDateTime loginTime,
        String ipAddress
) implements Serializable {
}
