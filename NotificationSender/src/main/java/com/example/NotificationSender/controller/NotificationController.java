package com.example.NotificationSender.controller;

import com.example.NotificationSender.model.NotificationRequest;
import com.example.NotificationSender.service.NotificationService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.redis.core.RedisTemplate;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private static final Logger LOGGER = Logger.getLogger(NotificationController.class.getName());
    private static final String TEMPLATE_PATH = "src/main/resources/templates/emailTemplate.html";
    private static final String RECIPIENT_PLACEHOLDER = "{{recipient}}";
    private static final String SUBJECT_PLACEHOLDER = "{{subject}}";
    private static final String MESSAGE_PLACEHOLDER = "{{message}}";

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Operation(summary = "Send an email notification")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email sent successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/email")
    public ResponseEntity<String> sendEmail(@RequestBody NotificationRequest request) {
        if (request.getTo() == null || request.getSubject() == null || request.getText() == null) {
            return ResponseEntity.badRequest().body("To, subject, and message cannot be null.");
        }

        try {
            // Check if the email content is already cached
            String cacheKey = "email:" + request.getTo();
            String htmlMessage;

            // Load HTML email template and replace placeholders
            String htmlTemplate = loadTemplate(TEMPLATE_PATH);
            if (htmlTemplate.isEmpty()) {
                return ResponseEntity.status(500).body("Error loading email template.");
            }

            // Prepare new email content
            htmlMessage = htmlTemplate
                    .replace(RECIPIENT_PLACEHOLDER, request.getTo())
                    .replace(SUBJECT_PLACEHOLDER, request.getSubject())
                    .replace(MESSAGE_PLACEHOLDER, request.getText());

            // Check if cached content is different from the new content
            String cachedContent = (String) redisTemplate.opsForValue().get(cacheKey);
            if (cachedContent == null || !cachedContent.equals(htmlMessage)) {
                // Cache the new email content
                redisTemplate.opsForValue().set(cacheKey, htmlMessage);
                LOGGER.info("Email content cached");
            } else {
                LOGGER.info("Email content found in cache, no update needed");
            }

            // Send email
            notificationService.sendEmail(request.getTo(), request.getSubject(), htmlMessage);

            // Send message to Kafka with the recipient's email as the key
            notificationService.sendNotificationToKafka(request.getTo(), htmlMessage);

            return ResponseEntity.ok("Email sent successfully and message sent to Kafka.");
        } catch (MessagingException e) {
            LOGGER.log(Level.SEVERE, "Error sending email", e);
            return ResponseEntity.status(500).body("Error sending email: " + e.getMessage());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error processing request", e);
            return ResponseEntity.status(500).body("Error processing request: " + e.getMessage());
        }
    }


    private String loadTemplate(String filePath) {
        try {
            Path path = Paths.get(filePath);
            return Files.readString(path); // Using readString for cleaner code (Java 11+)
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error loading template file", e);
            return "";
        }
    }
}



//package com.example.NotificationSender.controller;
//
//import com.example.NotificationSender.model.NotificationRequest;
//import com.example.NotificationSender.service.NotificationService;
//
//import jakarta.mail.MessagingException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.mail.MailAuthenticationException;
//import org.springframework.web.bind.annotation.*;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.responses.ApiResponses;
//import org.springframework.data.redis.core.RedisTemplate;
//
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//@RestController
//@RequestMapping("/notifications")
//public class NotificationController {
//
//    private static final Logger LOGGER = Logger.getLogger(NotificationController.class.getName());
//    private static final String TEMPLATE_PATH = "src/main/resources/templates/emailTemplate.html";
//    private static final String RECIPIENT_PLACEHOLDER = "{{recipient}}";
//    private static final String SUBJECT_PLACEHOLDER = "{{subject}}";
//    private static final String MESSAGE_PLACEHOLDER = "{{message}}";
//
//    @Autowired
//    private NotificationService notificationService;
//
//    @Autowired
//    private RedisTemplate<String, Object> redisTemplate;
//
//    @Operation(summary = "Send an email notification")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Email sent successfully"),
//            @ApiResponse(responseCode = "400", description = "Bad request"),
//            @ApiResponse(responseCode = "401", description = "Authentication failed"),
//            @ApiResponse(responseCode = "500", description = "Internal server error")
//    })
//    @PostMapping("/email")
//    public ResponseEntity<String> sendEmail(@RequestBody NotificationRequest request) {
//        if (request.getTo() == null || request.getSubject() == null || request.getText() == null) {
//            return ResponseEntity.badRequest().body("To, subject, and message cannot be null.");
//        }
//
//        try {
//            // Check if the email content is already cached
//            String cacheKey = "email:" + request.getTo();
//            String htmlMessage;
//
//            // Load HTML email template and replace placeholders
//            String htmlTemplate = loadTemplate(TEMPLATE_PATH);
//            if (htmlTemplate.isEmpty()) {
//                return ResponseEntity.status(500).body("Error loading email template.");
//            }
//
//            // Prepare new email content
//            htmlMessage = htmlTemplate
//                    .replace(RECIPIENT_PLACEHOLDER, request.getTo())
//                    .replace(SUBJECT_PLACEHOLDER, request.getSubject())
//                    .replace(MESSAGE_PLACEHOLDER, request.getText());
//
//            // Check if cached content is different from the new content
//            String cachedContent = (String) redisTemplate.opsForValue().get(cacheKey);
//            if (cachedContent == null || !cachedContent.equals(htmlMessage)) {
//                // Cache the new email content
//                redisTemplate.opsForValue().set(cacheKey, htmlMessage);
//                LOGGER.info("Email content cached.");
//            } else {
//                LOGGER.info("Email content found in cache, no update needed.");
//            }
//
//            // Send email
//            notificationService.sendEmail(request.getTo(), request.getSubject(), htmlMessage);
//
//            // Send message to Kafka with the recipient's email as the key
//            notificationService.sendNotificationToKafka(request.getTo(), htmlMessage);
//
//            return ResponseEntity.ok("Email sent successfully and message sent to Kafka.");
//        } catch (MailAuthenticationException e) {
//            LOGGER.log(Level.SEVERE, "Authentication failed", e);
//            return ResponseEntity.status(401).body("Authentication failed: " + e.getMessage());
//        } catch (MessagingException e) {
//            LOGGER.log(Level.SEVERE, "Error sending email", e);
//            return ResponseEntity.status(500).body("Error sending email: " + e.getMessage());
//        } catch (Exception e) {
//            LOGGER.log(Level.SEVERE, "Error processing request", e);
//            return ResponseEntity.status(500).body("Error processing request: " + e.getMessage());
//        }
//    }
//
//    private String loadTemplate(String filePath) {
//        try {
//            Path path = Paths.get(filePath);
//            return Files.readString(path); // Using readString for cleaner code (Java 11+)
//        } catch (Exception e) {
//            LOGGER.log(Level.SEVERE, "Error loading template file", e);
//            return "";
//        }
//    }
//}
