package com.sky.service.impl;

import org.springframework.stereotype.Service;

import com.sky.entity.Chat;
import com.sky.repository.ChatRepository;
import com.sky.service.ChatService;

@Service
public class ChatServiceImpl implements ChatService {

	private ChatRepository chatRepository;

	public ChatServiceImpl(ChatRepository chatRepository) {
		this.chatRepository = chatRepository;
	}

	@Override
	public Chat createChat(Chat chat) {
		Chat createdChat = new Chat();
		createdChat.setMessages(chat.getMessages());
		createdChat.setName(chat.getName());
		createdChat.setProject(chat.getProject());
		createdChat.setUsers(chat.getUsers());
		return chatRepository.save(createdChat);
	}
}
