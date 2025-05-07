package com.poorna.blogapp.WebSocketsPrivate;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class NotificationController {

    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/notify")
    public void sendNotification(NotificationDTO notification, Authentication authentication) {
        String receiverEmail = notification.getReciever(); // or pass receiver in DTO
        messagingTemplate.convertAndSendToUser(
                receiverEmail,
                "/queue/notifications",
                notification
        );
    }
}
