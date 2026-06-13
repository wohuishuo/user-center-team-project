package com.usercenter.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

/**
 * 登录事件发布。两条铁律:
 * 1. 异步发送——发布事件绝不拖慢登录响应;
 * 2. 失败只记日志——MQ 宕机不影响用户登录(统计是旁路,不是主链路)。
 */
@Component
public class UserEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(UserEventPublisher.class);

    /** fanout 交换机名,与统计服务(br-event-stats)约定一致。 */
    public static final String EXCHANGE_USER_EVENTS = "user.events";

    private final RabbitTemplate rabbitTemplate;

    public UserEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishLogin(UserLoginEvent event) {
        CompletableFuture.runAsync(() -> {
            try {
                rabbitTemplate.convertAndSend(EXCHANGE_USER_EVENTS, "", event);
                log.info("UserLogin event published: userId={}, type={}", event.userId(), event.loginType());
            } catch (Exception e) {
                log.warn("UserLogin event publish failed (login NOT affected): {}", e.getMessage());
            }
        });
    }

    /** 事件以 JSON 发送;交换机由生产者侧也声明一次(幂等),避免依赖消费者先启动。 */
    @Configuration
    static class AmqpConfig {
        @Bean
        Jackson2JsonMessageConverter jacksonMessageConverter(com.fasterxml.jackson.databind.ObjectMapper springMapper) {
            // 用 Spring Boot 配好的 ObjectMapper(含 jsr310,LocalDateTime 才能序列化)
            return new Jackson2JsonMessageConverter(springMapper);
        }

        @Bean
        FanoutExchange userEventsExchange() {
            return new FanoutExchange(EXCHANGE_USER_EVENTS, true, false);
        }
    }
}
