package com.example.NotificationSender.service;
//
//import jakarta.mail.MessagingException;
//import jakarta.mail.internet.MimeMessage;
//import org.apache.kafka.clients.admin.AdminClient;
//import org.apache.kafka.clients.admin.NewTopic;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cache.annotation.Cacheable;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessageHelper;
//import org.springframework.stereotype.Service;
//
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.Collections;
//
//@Service
//public class NotificationService {
//
//    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
//
//    @Autowired
//    private JavaMailSender mailSender;
//
//    @Autowired
//    private KafkaTemplate<String, String> kafkaTemplate;
//
//    @Autowired
//    private AdminClient adminClient;
//
//    private final String TOPIC = "notification"; // Replace with your Kafka topic
//
//    public void sendEmail(String to, String subject, String htmlMessage) throws MessagingException {
//        MimeMessage mimeMessage = mailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
//
//        try {
//            helper.setTo(to);
//            helper.setSubject(subject);
//            helper.setText(htmlMessage, true); // Set to true to send HTML
//            mailSender.send(mimeMessage);
//            logger.info("Email sent successfully to: {}", to);
//        } catch (MessagingException e) {
//            logger.error("Error sending email to {}: {}", to, e.getMessage());
//            throw e; // Re-throw the exception for higher-level handling
//        }
//    }
//
//    private void createTopicIfNotExists(String topicName) {
//        try {
//            if (!adminClient.listTopics().names().get().contains(topicName)) {
//                NewTopic newTopic = new NewTopic(topicName, 1, (short) 1); // 1 partition, 1 replication factor
//                adminClient.createTopics(Collections.singleton(newTopic)).all().get();
//                logger.info("Topic created: {}", topicName);
//            }
//        } catch (Exception e) {
//            logger.error("Error creating topic: {}", e.getMessage());
//        }
//    }
//
//    public void sendNotificationToKafka(String key, String message) {
//        createTopicIfNotExists(TOPIC); // Ensure the topic exists before sending
//        kafkaTemplate.send(TOPIC, key, message); // Send the key and message to Kafka
//        logger.info("Notification sent to Kafka topic '{}': key='{}', message='{}'", TOPIC, key, message);
//    }
//
//    @Cacheable(value = "emailTemplate", key = "#templatePath")
//    public String loadTemplate(String templatePath) {
//        try {
//            Path path = Paths.get(templatePath);
//            return Files.readString(path); // Using readString for cleaner code (Java 11+)
//        } catch (Exception e) {
//            logger.error("Error loading template file", e);
//            return "";
//        }
//    }
//}

import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

@Service
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    private static final String TOPIC = "notification2"; // Replace with your Kafka topic

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private AdminClient adminClient;

    @PostConstruct
    public void init() {
        createTopicIfNotExists(TOPIC); // Create topic on startup
    }

    public void sendEmail(String to, String subject, String htmlMessage) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        try {
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlMessage, true); // Set to true to send HTML
            mailSender.send(mimeMessage);
            logger.info("Email sent successfully to: {}", to);
        } catch (MessagingException e) {
            logger.error("Error sending email to {}: {}", to, e.getMessage());
            throw e; // Re-throw the exception for higher-level handling
        }
    }

    private void createTopicIfNotExists(String topicName) {
        try {
            if (!adminClient.listTopics().names().get().contains(topicName)) {
                NewTopic newTopic = new NewTopic(topicName, 1, (short) 1); // 1 partition, 1 replication factor
                adminClient.createTopics(Collections.singleton(newTopic)).all().get();
                logger.info("Topic created: {}", topicName);
            } else {
                logger.info("Topic already exists: {}", topicName);
            }
        } catch (Exception e) {
            logger.error("Error creating topic: {}", e.getMessage(), e);
        }
    }

    public void sendNotificationToKafka(String key, String message) {
        kafkaTemplate.send(TOPIC, key, message); // Send the key and message to Kafka
        logger.info("Notification sent to Kafka topic '{}': key='{}', message='{}'", TOPIC, key, message);
    }

    @Cacheable(value = "emailTemplate", key = "#templatePath")
    public String loadTemplate(String templatePath) {
        try {
            Path path = Paths.get(templatePath);
            return Files.readString(path); // Using readString for cleaner code (Java 11+)
        } catch (Exception e) {
            logger.error("Error loading template file: {}", e.getMessage(), e);
            return ""; // Return empty string on error
        }
    }
}
