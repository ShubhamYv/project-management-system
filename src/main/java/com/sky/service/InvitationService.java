package com.sky.service;

import com.sky.entity.Invitation;

public interface InvitationService {

	void sendInvitation(String email, Long projectId);

	Invitation acceptInvitation(String token, Long userId);

	String getTokenByUserEmail(String userEmail);
	
	void deleteToken(String token);
}
