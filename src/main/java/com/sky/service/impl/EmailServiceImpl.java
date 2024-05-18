package com.sky.service.impl;

import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.sky.constants.MailConstants;
import com.sky.service.EmailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {

	private JavaMailSender javaMailSender;

	public EmailServiceImpl(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}

	@Override
	public void sendEmailWithToken(String userEmail, String link) throws MessagingException {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, "utf-8");

		String subject = MailConstants.SUBJECT;
		String text = MailConstants.TEXT + link;
		messageHelper.setSubject(subject);
		messageHelper.setText(text, true);
		messageHelper.setTo(userEmail);

		try {
			javaMailSender.send(mimeMessage);
		} catch (MailSendException e) {
			throw new MailSendException("Failed to send email");
		}
	}

}
