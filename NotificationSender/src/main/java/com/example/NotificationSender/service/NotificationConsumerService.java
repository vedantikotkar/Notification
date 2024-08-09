package com.example.NotificationSender.service;



import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationConsumerService {

    @KafkaListener(topics = "notification2", groupId = "notification_group")
    public void consume(String message) {
        // Process the message and send notifications (email, SMS, push)
        System.out.println("Received Message in group notification_group: " + message);
        // Implement the logic to send email, SMS, or push notifications here
    }
}
