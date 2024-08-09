package com.example.NotificationSender.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class NotificationProducerService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private static final String TOPIC = "notification2";
    private static final String CACHE_KEY_PREFIX = "notification2:";

    public void sendMessage(String key, String message) {
        try {
            // Cache the message in Redis before sending it to Kafka
            cacheMessage(key, message);
            kafkaTemplate.send(TOPIC, key, message); // Use the key when sending the message
            System.out.println("Message sent to Kafka: " + message);
        } catch (Exception e) {
            System.err.println("Error sending message to Kafka: " + e.getMessage());
            // Optionally, log the error or rethrow it
        }
    }

    private void cacheMessage(String key, String message) {
        String cacheKey = CACHE_KEY_PREFIX + key;
        try {
            redisTemplate.opsForValue().set(cacheKey, message, Duration.ofMinutes(10)); // Set expiration time as needed
            System.out.println("Message cached in Redis: " + message);
        } catch (Exception e) {
            System.err.println("Error caching message in Redis: " + e.getMessage());
        }
    }

}
