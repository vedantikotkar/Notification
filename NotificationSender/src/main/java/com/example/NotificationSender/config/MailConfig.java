////package com.example.NotificationSender.config;
////
////import org.springframework.context.annotation.Bean;
////import org.springframework.context.annotation.Configuration;
////import org.springframework.mail.javamail.JavaMailSender;
////import org.springframework.mail.javamail.JavaMailSenderImpl;
////
////import java.util.Properties;
////
////@Configuration
////public class MailConfig {
////
////    @Bean
////    public JavaMailSender javaMailSender() {
////        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
////        mailSender.setHost("smtp.gmail.com"); // Your SMTP server
////        mailSender.setPort(587); // SMTP port
////
////        mailSender.setUsername("nikhildighe32@gmail.com");
////        mailSender.setPassword("lgkb sext kwoc xcjy");
////
////        Properties props = mailSender.getJavaMailProperties();
////        props.put("mail.transport.protocol", "smtp");
////        props.put("mail.smtp.auth", "true");
////        props.put("mail.starttls.enable", "true");
////        props.put("mail.debug", "true");
////
////        return mailSender;
////    }
////}
//
//
//
//
package com.example.NotificationSender.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.SecureRandom;
import java.util.Properties;

@Configuration
public class MailConfig {

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        mailSender.setUsername("nikhildighe32@gmail.com"); // Use your email
        mailSender.setPassword("fayd oast khsi yqkt"); // Use your app password

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); // Enable STARTTLS
        props.put("mail.debug", "true");
        props.put("mail.smtp.ssl.trust", "*");

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[] { new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() { return null; }
                public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {}
                public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {}
            }}, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mailSender;
    }

}




//import org.springframework.mail.javamail.JavaMailSenderImpl;
//
//import javax.net.ssl.SSLContext;
//import javax.net.ssl.TrustManager;
//import javax.net.ssl.X509TrustManager;
//import java.security.cert.X509Certificate;
//import java.util.Properties;
//
//public class MailConfig{
//
//    public JavaMailSenderImpl getJavaMailSender() {
//        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
//        mailSender.setHost("smtp.gmail.com");
//        mailSender.setPort(587);
//        mailSender.setUsername("nikhildighe32@gmail.com");
//        mailSender.setPassword("lgkb sext kwoc xcjy");
//
//        Properties props = mailSender.getJavaMailProperties();
//        props.put("mail.transport.protocol", "smtp");
//        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.starttls.enable", "true");
//        props.put("mail.smtp.ssl.trust", "*");  // Trust all certificates
//
//        // Set up a custom TrustManager
//        try {
//            SSLContext sslContext = SSLContext.getInstance("TLS");
//            sslContext.init(null, new TrustManager[] { new X509TrustManager() {
//                public X509Certificate[] getAcceptedIssuers() { return null; }
//                public void checkClientTrusted(X509Certificate[] certs, String authType) {}
//                public void checkServerTrusted(X509Certificate[] certs, String authType) {}
//            }}, new java.security.SecureRandom());
//            props.put("mail.smtp.ssl.socketFactory", sslContext.getSocketFactory());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return mailSender;
//    }
//}
