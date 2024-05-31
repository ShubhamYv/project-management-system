package com.sky.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.sky.constants.ErrorCodeEnum;
import com.sky.constants.MailConstants;
import com.sky.exception.ProjectManagementException;
import com.sky.service.EmailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sendEmailWithToken(String userEmail, String link) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, "utf-8");

            String subject = MailConstants.SUBJECT;
            String text = MailConstants.TEXT + link;

            messageHelper.setSubject(subject);
            messageHelper.setText(text, true);
            messageHelper.setTo(userEmail);

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new ProjectManagementException(
                ErrorCodeEnum.EMAIL_SEND_FAILED.getErrorMessage(),
                ErrorCodeEnum.EMAIL_SEND_FAILED.getErrorCode(),
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        } catch (MailSendException e) {
            throw new ProjectManagementException(
                ErrorCodeEnum.EMAIL_SEND_FAILED.getErrorMessage(),
                ErrorCodeEnum.EMAIL_SEND_FAILED.getErrorCode(),
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}
