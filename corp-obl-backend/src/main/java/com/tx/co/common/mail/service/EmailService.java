package com.tx.co.common.mail.service;

import java.io.File;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailService implements IEmailService {

	private static final Logger logger = LogManager.getLogger(EmailService.class);
	
	@Autowired
	public JavaMailSender emailSender;

	public void sendSimpleMessage(String to, String cc, String bcc, String subject, String text) {
		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setTo(to);
			message.setCc(cc);
			message.setBcc(bcc);
			message.setSubject(subject);
			message.setText(text);

			emailSender.send(message);
		} catch (MailException exception) {
			logger.error(exception);
		} catch (Exception e) {
			logger.error(e);
		}
	}

	@Override
	public void sendSimpleMessageUsingTemplate(String to, String cc, String bcc,  String subject, SimpleMailMessage template, String ...templateArgs) {
		String text = template.getText() + templateArgs;  
		sendSimpleMessage(to, cc, bcc, subject, text);
	}

	@Override
	public void sendMessageWithAttachment(String to, String cc, String bcc,  String subject, String text, String pathToAttachment) {
		try {
			MimeMessage message = emailSender.createMimeMessage();
			// pass 'true' to the constructor to create a multipart message
			MimeMessageHelper helper = new MimeMessageHelper(message, true);

			helper.setTo(to);
			helper.setCc(cc);
			helper.setBcc(bcc);
			helper.setSubject(subject);
			helper.setText(text);

			FileSystemResource file = new FileSystemResource(new File(pathToAttachment));
			helper.addAttachment("Invoice", file);

			emailSender.send(message);
		} catch (MessagingException e) {
			logger.error(e);
		}
	}

}
