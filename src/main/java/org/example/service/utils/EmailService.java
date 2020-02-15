package org.example.service.utils;

import org.example.exception.AppException;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailService {

    private static final String EMAIL_ADDRESS = "cossica123123@gmail.com";
    private static final String EMAIL_PASSWORD = "Bi@nk@11";


    public void send(String to, String title, String html) {
        try {
            System.out.println("EMAIL SENDING ....");
            Session session = createSession();
            MimeMessage mimeMessage = new MimeMessage(session);
            prepareEmailMessage(mimeMessage, to, title, html);
            Transport.send(mimeMessage);
            System.out.println("EMAIL HAS BEEN SENT");
        } catch (Exception e) {
            throw new AppException("email send exception");
        }
    }


    private  void prepareEmailMessage(MimeMessage mimeMessage, String to, String title, String html) {
        try {
            mimeMessage.setContent(html, "text/html; charset=utf-8");
            mimeMessage.setFrom(new InternetAddress(EMAIL_ADDRESS));
            mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            mimeMessage.setSubject(title);
        } catch (Exception e) {
            throw new AppException("prepare email message");
        }
    }

    private Session createSession() {
        Properties properties = new Properties();
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");

        return Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_ADDRESS, EMAIL_PASSWORD);
            }
        });
    }
}
