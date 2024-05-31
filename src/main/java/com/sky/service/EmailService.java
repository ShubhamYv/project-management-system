package com.sky.service;

public interface EmailService {

	void sendEmailWithToken(String userEmail, String link);
	
}
