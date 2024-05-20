package com.sky.service.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.sky.entity.Invitation;
import com.sky.repository.InvitationRepository;
import com.sky.service.EmailService;
import com.sky.service.InvitationService;

import jakarta.mail.MessagingException;

@Service
public class InvitationServiceImpl implements InvitationService {

	private InvitationRepository invitationRepository;
	private EmailService emailService;

	public InvitationServiceImpl(InvitationRepository invitationRepository, EmailService emailService) {
		this.invitationRepository = invitationRepository;
		this.emailService = emailService;
	}

	@Override
	public void sendInvitation(String email, Long projectId) throws MessagingException {
		String invitationToken = UUID.randomUUID().toString();
		Invitation invitation = new Invitation();
		invitation.setEmail(email);
		invitation.setProjectId(projectId);
		invitation.setToken(invitationToken);
		invitationRepository.save(invitation);

		String invitationLink = "http://localhost:3000/accept_invitation?token=" + invitationToken;
		emailService.sendEmailWithToken(email, invitationLink);
	}

	@Override
	public Invitation acceptInvitation(String token, Long userId) throws Exception {
		Invitation invitation = invitationRepository.findByToken(token);
		if (null == invitation) {
			throw new Exception("Invalid invitation token");
		}
		return invitation;
	}

	@Override
	public String getTokenByUserEmail(String userEmail) {
		Invitation invitation = invitationRepository.findByEmail(userEmail);
		return invitation.getToken();
	}

	@Override
	public void deleteToken(String token) {
		Invitation invitation = invitationRepository.findByToken(token);
		invitationRepository.delete(invitation);
	}

}
