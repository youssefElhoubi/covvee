package com.covvee.config;

import com.covvee.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WebSocketAuthInterceptor implements ChannelInterceptor {
    private final JwtUtils jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        // 1. Handle initial connection and token validation
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {

            // Grab the token from the STOMP header
            String authHeader = accessor.getFirstNativeHeader("Authorization");

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);

                // Extract username. Change this method call to match your JWT utility!
                String username = jwtService.extractUsername(token);

                if (username != null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    // Validate token. Change this method call to match your JWT utility!
                    if (jwtService.validateToken(token)) {

                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(
                                        userDetails, null, userDetails.getAuthorities()
                                );

                        // Tells Spring Messaging who the user is
                        accessor.setUser(authentication);
                        System.out.println("🎯 CONNECT: Token valid! User set to -> " + authentication.getName());
                    }
                }
            }
        }

        // 2. THE MISSING BRIDGE:
        // For ALL incoming messages (not just CONNECT), copy the user from the
        // WebSocket session into Spring Security's ThreadLocal context.
        if (accessor != null && accessor.getUser() != null) {
            SecurityContextHolder.getContext().setAuthentication((Authentication) accessor.getUser());
        }
        System.out.println("🌉 BRIDGE: Command -> " + accessor.getCommand() +
                " | User -> " + accessor.getUser() +
                " | Thread -> " + Thread.currentThread().getName());

        return message;
    }

    // 3. CRITICAL CLEANUP:
    // Worker threads are pooled! You MUST clear the context after the message is sent,
    // otherwise User A's credentials might accidentally authorize User B's websocket message.
    @Override
    public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {
        SecurityContextHolder.clearContext();
    }
}