package com.sky.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.sky.dto.ChatDTO;
import com.sky.entity.Chat;
import com.sky.repository.ChatRepository;
import com.sky.service.ChatService;

@Service
public class ChatServiceImpl implements ChatService {

	private final ChatRepository chatRepository;
	private final ModelMapper modelMapper;

	public ChatServiceImpl(ChatRepository chatRepository, ModelMapper modelMapper) {
		this.chatRepository = chatRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	public ChatDTO createChat(ChatDTO chatDTO) {
		Chat chat = modelMapper.map(chatDTO, Chat.class);
		Chat savedChat = chatRepository.save(chat);
		return modelMapper.map(savedChat, ChatDTO.class);
	}
}
