package com.blue.tnb.notification;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;


public class NotificationEmail implements Runnable {

    private String subject;
    private String message;
    private String email;
    private JavaMailSender mailSender;

    public NotificationEmail(String subject, String message, String email, JavaMailSender mailSender) {
        this.subject = subject;
        this.message = message;
        this.email = email;
        this.mailSender = mailSender;
    }

    @Override
    public void run() {
        final SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(email);
        mail.setSubject(subject);
        mail.setText(message);
//        mail.setFrom(Objects.requireNonNull(env.getProperty("support.email")));
        mail.setFrom("TNBTickets");
        mailSender.send(mail);
        System.out.println("EMAIL SENT TO:    " + Thread.currentThread().getName());
    }
}
