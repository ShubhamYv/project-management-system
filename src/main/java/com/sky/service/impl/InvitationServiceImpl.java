package com.sky.service.impl;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.sky.constants.ErrorCodeEnum;
import com.sky.entity.Invitation;
import com.sky.exception.ProjectManagementException;
import com.sky.repository.InvitationRepository;
import com.sky.service.EmailService;
import com.sky.service.InvitationService;

@Service
public class InvitationServiceImpl implements InvitationService {

    private final InvitationRepository invitationRepository;
    private final EmailService emailService;

    public InvitationServiceImpl(InvitationRepository invitationRepository, EmailService emailService) {
        this.invitationRepository = invitationRepository;
        this.emailService = emailService;
    }

    @Override
    public void sendInvitation(String email, Long projectId) {
        try {
            String invitationToken = UUID.randomUUID().toString();
            Invitation invitation = new Invitation();
            invitation.setEmail(email);
            invitation.setProjectId(projectId);
            invitation.setToken(invitationToken);
            invitationRepository.save(invitation);

            String invitationLink = "http://localhost:3000/accept_invitation?token=" + invitationToken;
            emailService.sendEmailWithToken(email, invitationLink);
        } catch (Exception e) {
            throw new ProjectManagementException(
                ErrorCodeEnum.INVITATION_SEND_FAILED.getErrorMessage(),
                ErrorCodeEnum.INVITATION_SEND_FAILED.getErrorCode(),
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @Override
    public Invitation acceptInvitation(String token, Long userId) {
        Invitation invitation = invitationRepository.findByToken(token);
        if (invitation == null) {
            throw new ProjectManagementException(
                ErrorCodeEnum.INVALID_INVITATION_TOKEN.getErrorMessage(),
                ErrorCodeEnum.INVALID_INVITATION_TOKEN.getErrorCode(),
                HttpStatus.BAD_REQUEST
            );
        }
        return invitation;
    }

    @Override
    public String getTokenByUserEmail(String userEmail) {
        Invitation invitation = invitationRepository.findByEmail(userEmail);
        if (invitation == null) {
            throw new ProjectManagementException(
                ErrorCodeEnum.USER_NOT_FOUND.getErrorMessage(),
                ErrorCodeEnum.USER_NOT_FOUND.getErrorCode(),
                HttpStatus.NOT_FOUND
            );
        }
        return invitation.getToken();
    }

    @Override
    public void deleteToken(String token) {
        Invitation invitation = invitationRepository.findByToken(token);
        if (invitation == null) {
            throw new ProjectManagementException(
                ErrorCodeEnum.INVALID_INVITATION_TOKEN.getErrorMessage(),
                ErrorCodeEnum.INVALID_INVITATION_TOKEN.getErrorCode(),
                HttpStatus.BAD_REQUEST
            );
        }
        invitationRepository.delete(invitation);
    }
}
