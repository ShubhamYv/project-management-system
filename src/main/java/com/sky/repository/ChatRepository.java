package com.sky.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sky.entity.Chat;

public interface ChatRepository extends JpaRepository<Chat, Long> {

}
