package com.example.cargo.service;

import com.example.cargo.model.NotificationMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public NotificationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendNotification(String message) {
        NotificationMessage notification = new NotificationMessage(message);
        // /topic/notifications kanalına yayın yapar
        messagingTemplate.convertAndSend("/topic/notifications", notification);
    }
}
