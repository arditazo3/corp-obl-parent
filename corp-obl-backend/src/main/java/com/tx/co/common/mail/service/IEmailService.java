package com.tx.co.common.mail.service;

import org.springframework.mail.SimpleMailMessage;

public interface IEmailService {

    public void sendSimpleMessage(String to, String cc, String bcc, String subject, String text);

    void sendSimpleMessageUsingTemplate(String to, String cc, String bcc, String subject, SimpleMailMessage template, String... templateArgs);

    void sendMessageWithAttachment(String to, String cc, String bcc, String subject, String text, String pathToAttachment);
}
