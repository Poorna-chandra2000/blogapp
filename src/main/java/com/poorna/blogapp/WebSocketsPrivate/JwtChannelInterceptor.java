package com.poorna.blogapp.WebSocketsPrivate;

import com.poorna.blogapp.SecurityApp.JwtUtils.JwtService;
import com.poorna.blogapp.SecurityApp.SecurityService.UserService;
import com.poorna.blogapp.SecurityApp.User;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@RequiredArgsConstructor
public class JwtChannelInterceptor implements ChannelInterceptor {

    private final JwtService jwtService;
    private final UserService userService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authHeader = accessor.getFirstNativeHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                if (jwtService.validateToken(token)) {
                    Long userId = jwtService.getUserIdFromToken(token);
                    User user = userService.getUserById(userId);
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(user.getEmail(), null, user.getAuthorities());
                    accessor.setUser(authentication);
                    // Log for debugging
                    System.out.println("User authenticated: " + user.getEmail());
                } else {
                    // Log if token is invalid
                    System.out.println("Invalid token");
                }
            } else {
                // Log if no Authorization header is found
                System.out.println("Authorization header missing");
            }
        }
        return message;
    }


    }

