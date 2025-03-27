package com.ims.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class MailService {

//    @Autowired
//    private JavaMailSender mailSender;
//
//    public void sendEmailWithAttachment(String to, String subject, String body, String attachmentPath) throws MessagingException {
//        MimeMessage message = mailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(message, true);
//
//        helper.setTo(to);
//        helper.setSubject(subject);
//        helper.setText(body);
//
//        FileSystemResource file = new FileSystemResource(new File(attachmentPath));
//        helper.addAttachment(file.getFilename(), file);
//
//        mailSender.send(message);
//        System.out.println("Mail Sent Successfully with Attachment");
//    }
}
