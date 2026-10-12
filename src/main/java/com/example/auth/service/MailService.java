package com.example.auth.service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
@Service public class MailService {
private final JavaMailSender sender;
@Value("${app.mail.from}") private String from;
public MailService(JavaMailSender sender){this.sender=sender;}
public void sendReset(String email,String url){SimpleMailMessage message=new SimpleMailMessage();message.setFrom(from);message.setTo(email);message.setSubject("Reset your password");message.setText("Use this link within 15 minutes to reset your password:\n"+url+"\nIf you did not request this, ignore this email.");sender.send(message);}
}
