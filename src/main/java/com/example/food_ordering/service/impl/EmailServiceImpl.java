package com.example.food_ordering.service.impl;

import com.example.food_ordering.dto.UserDto;
import com.example.food_ordering.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendVerificationEmail(UserDto userDto, String token) {
        String subject = "MustFood verification email";
        String senderName = "MustFood";
        String confirmationLink = "http://localhost:8080/api/auth/verify?token=" + token;

        String mailContent = "<p> Dear " +userDto.getUsername() + ", </p>";
        mailContent += "<p>Please click the link below to verify your email </p>";
        mailContent += "<h3><a href=\"" + confirmationLink + "\">VERIFY</a></h3>";
        mailContent += "<p>Thank you <br> M-Travel </p>";

        try {
            jakarta.mail.internet.MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

            helper.setFrom("admin@mustfood.com",senderName);
            helper.setTo(userDto.getEmail());
            helper.setSubject(subject);
            helper.setText(mailContent,true);

            mailSender.send(mimeMessage);
        }  catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (jakarta.mail.MessagingException e) {
            throw new RuntimeException(e);
        }
    }

}





















