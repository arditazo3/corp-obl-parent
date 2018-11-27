package com.tx.co.common.mail.model;

import org.springframework.mail.SimpleMailMessage;

/**
 * Model that represents a mail.
 *
 * @author aazo
 */
public class Mail {

	private String to;
	private String cc;
	private String bcc;
	private String subject;
	private String text;
	private String mailTemplate;
	private SimpleMailMessage template;
	private String pathToAttachment;
	
	public Mail() {
	}
	public Mail(String to, String cc, String bcc, String subject, String text, String mailTemplate) {
		this.to = to;
		this.cc = cc;
		this.bcc = bcc;
		this.subject = subject;
		this.text = text;
		this.mailTemplate = mailTemplate;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getCc() {
		return cc;
	}
	public void setCc(String cc) {
		this.cc = cc;
	}
	public String getBcc() {
		return bcc;
	}
	public void setBcc(String bcc) {
		this.bcc = bcc;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public SimpleMailMessage getTemplate() {
		return template;
	}
	public void setTemplate(SimpleMailMessage template) {
		this.template = template;
	}
	public String getPathToAttachment() {
		return pathToAttachment;
	}
	public void setPathToAttachment(String pathToAttachment) {
		this.pathToAttachment = pathToAttachment;
	}
	public String getMailTemplate() {
		return mailTemplate;
	}
	public void setMailTemplate(String mailTemplate) {
		this.mailTemplate = mailTemplate;
	}
	
	
}
