package com.userService.service;


import com.userService.entity.User;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;

@Service
@Slf4j
@AllArgsConstructor
public class MailService {


    public void sendActivationEmail(User user, String subject,String message) {

        try {
            // Create a custom JavaMailSender with Authenticator
            JavaMailSenderImpl mailSenderImpl = new JavaMailSenderImpl();
            mailSenderImpl.setHost("smtp.gmail.com");
            mailSenderImpl.setPort(587);
            mailSenderImpl.setUsername("nourghozzi9@gmail.com");
            mailSenderImpl.setPassword("rkrj ndgr jgal xral"); // App Password

            Properties props = mailSenderImpl.getJavaMailProperties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.debug", "true");

            // Set up the session with authentication
            Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("nourghozzi9@gmail.com", "rkrj ndgr jgal xral");
                }
            });

            session.setDebug(true);

            // Create the MimeMessage
            MimeMessage mimeMessage = mailSenderImpl.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(user.getEmail());
            helper.setSubject(subject);
            helper.setText(message);

            // Send the email
            mailSenderImpl.send(mimeMessage);

            log.info("Activation email sent to: {}", user.getEmail());
        } catch (Exception e) {
            log.error("Failed to send activation email to: {}", user.getEmail(), e);
        }
    }

}
