package com.sky.service;

import com.sky.entity.Invitation;

import jakarta.mail.MessagingException;

public interface InvitationService {

	void sendInvitation(String email, Long projectId) throws MessagingException;

	Invitation acceptInvitation(String token, Long userId) throws Exception;

	String getTokenByUserEmail(String userEmail);
	
	void deleteToken(String token);
}
