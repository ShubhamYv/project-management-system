package com.sky.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sky.service.EmailService;

import jakarta.mail.MessagingException;

@RestController
public class EmailController {

	@Autowired
	private EmailService emailService;

	@PostMapping("/send-mail")
	public ResponseEntity<String> testMail() throws MessagingException {
		emailService.sendEmailWithToken("shubhamyadav32100@gmail.com", "www.google.com");
		return new ResponseEntity<>("Mail Sent successfully", HttpStatus.OK);
	}
}
