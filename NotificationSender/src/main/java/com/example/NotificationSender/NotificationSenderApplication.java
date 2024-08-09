package com.example.NotificationSender;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NotificationSenderApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotificationSenderApplication.class, args);
	}

}

























//com.example.NotificationSender
//├── config
//│   ├── MailConfig.java
//│   ├── SwaggerConfig.java
//│   └── KafkaConfig.java
//├── controller
//│   └── NotificationController.java
//├── model
//│   └── NotificationRequest.java
//├── service
//│   └── NotificationService.java
//└── NotificationSenderApplication.java


//http://localhost:8080/swagger-ui/index.htmlx



//POST http://localhost:8080/api/notifications

//{
//		"userId": "12345",
//		"message": "Your notification message here.",
//		"type": "email" // or "sms", "push"
//		}




//http://localhost:8080/api/notifications/send

//{
//		"userId": "recipient@example.com",
//		"message": "This is a test email notification.",
//		"type": "EMAIL"
//		}
