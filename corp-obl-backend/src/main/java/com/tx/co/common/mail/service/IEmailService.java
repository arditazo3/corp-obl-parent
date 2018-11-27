package com.tx.co.common.mail.service;

import com.tx.co.common.mail.model.Mail;

public interface IEmailService {

    public void sendMail(Mail mail);

    void sendMailUsingTemplate(Mail mail);

    void sendMailWithAttachment(Mail mail);
}
