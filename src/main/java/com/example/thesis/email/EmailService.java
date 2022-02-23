package com.example.thesis.email;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@AllArgsConstructor
public class EmailService{

//    private final static Logger logger = LoggerFactory.getLogger(EmailService.class);
//
//    private final JavaMailSender mailSender;
//
//    @Override
//    @Async
//    public void send(String to, String email) {
//        try {
//            MimeMessage mimeMessage = mailSender.createMimeMessage();
//
//            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
//            helper.setText(email, true);
//            helper.setTo(to);
//            helper.setSubject("Confirm your email");
//            helper.setFrom("bao.truonganh1230@hcmut.edu.vn");
//        } catch(MessagingException e) {
//            logger.error("fail to send email", e);
//            throw new IllegalStateException("fail to send email");
//        }
//    }
}
