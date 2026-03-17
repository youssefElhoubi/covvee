package com.covvee.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.socket.EnableWebSocketSecurity;
import org.springframework.security.messaging.access.intercept.MessageMatcherDelegatingAuthorizationManager;

@Configuration
@EnableWebSocketSecurity
public class WebSocketSecurityConfig {

    @Bean
    public AuthorizationManager<Message<?>> messageAuthorizationManager(MessageMatcherDelegatingAuthorizationManager.Builder messages) {
        return messages
                // We permit all messages at the channel level because YOU want to use
                // @PreAuthorize to handle the security at the controller level!
                .anyMessage().permitAll()
                .build();
    }

    // 🚨 THE ANTI-SUFFERING BEAN 🚨
    // Spring Security WebSockets expects CSRF tokens. You use JWTs.
    // If we don't mock this bean, Spring will block every message and give you no errors.
    @Bean(name = "csrfChannelInterceptor")
    public ChannelInterceptor csrfChannelInterceptor() {
        return new ChannelInterceptor() {
        }; // Returns an empty interceptor, disabling CSRF for WebSockets
    }
}