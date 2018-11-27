package com.tx.co.common.mail.service;

import static com.tx.co.common.constants.AppConstants.*;
import static org.springframework.util.ObjectUtils.isEmpty;

import java.io.File;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

import com.tx.co.common.mail.model.Mail;

@Component
public class EmailService implements IEmailService {

	private static final Logger logger = LogManager.getLogger(EmailService.class);

	private JavaMailSender emailSender;
	private MailContentBuilder mailContentBuilder;

	@Autowired
	public void setEmailSender(JavaMailSender emailSender) {
		this.emailSender = emailSender;
	}

	@Autowired
	public void setMailContentBuilder(MailContentBuilder mailContentBuilder) {
		this.mailContentBuilder = mailContentBuilder;
	}

	public void sendMail(Mail mail) {
		
		MimeMessagePreparator messagePreparator = mimeMessage -> {
	        
			MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
	        messageHelper.setFrom(MAIL_SEND_FROM);
	        
	        messageHelper.setTo(mail.getTo());
	    	if (!isEmpty(mail.getCc())) {
	    		messageHelper.setCc(mail.getCc());	
	    	}
	    	if (!isEmpty(mail.getBcc())) {
	    		messageHelper.setBcc(mail.getBcc());	
	    	}
	    	if (!isEmpty(mail.getSubject())) {
	    		messageHelper.setSubject(mail.getSubject());	
	    	}
	        
	        messageHelper.setText(mail.getText(), true);
	    };
	    try {
	    	emailSender.send(messagePreparator);
	    } catch (Exception e) {
	    	logger.error(e);
	    }
	}

	@Override
	public void sendMailUsingTemplate(Mail mail) {

		String text = mailContentBuilder.build(mail.getMailTemplate(), mail.getText());
		mail.setText(text);

		sendMail(mail);
	}

	@Override
	public void sendMailWithAttachment(Mail mail) {
		try {
			MimeMessage message = emailSender.createMimeMessage();
			// pass 'true' to the constructor to create a multipart message
			MimeMessageHelper helper = new MimeMessageHelper(message, true);

			helper.setTo(mail.getTo());
			helper.setCc(mail.getCc());
			helper.setBcc(mail.getBcc());
			helper.setSubject(mail.getSubject());
			helper.setText(mail.getText(), true);

			FileSystemResource file = new FileSystemResource(new File(mail.getPathToAttachment()));
			helper.addAttachment("Invoice", file);

			emailSender.send(message);
		} catch (MessagingException e) {
			logger.error(e);
		}
	}

}
