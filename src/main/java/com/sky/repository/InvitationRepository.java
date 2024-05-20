package com.sky.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sky.entity.Invitation;

public interface InvitationRepository extends JpaRepository<Invitation, Long> {

	Invitation findByToken(String token);
	
	Invitation findByEmail(String userEmail);
}
