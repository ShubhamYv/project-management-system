package com.sky.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.sky.constants.ErrorCodeEnum;
import com.sky.dto.ChatDTO;
import com.sky.dto.MessageDTO;
import com.sky.entity.Chat;
import com.sky.entity.Message;
import com.sky.entity.User;
import com.sky.exception.ProjectManagementException;
import com.sky.repository.MessageRepository;
import com.sky.repository.UserRepository;
import com.sky.service.MessageService;
import com.sky.service.ProjectService;

@Service
public class MessageServiceImpl implements MessageService {
	
	private MessageRepository messageRepository;
	private UserRepository userRepository;
	private ProjectService projectService;
	private ModelMapper modelMapper;

	public MessageServiceImpl(MessageRepository messageRepository, UserRepository userRepository, 
			ProjectService projectService, ModelMapper modelMapper) {
		this.messageRepository = messageRepository;
		this.userRepository = userRepository;
		this.projectService = projectService;
		this.modelMapper = modelMapper;
	}
	
	@Override
	public MessageDTO sendMessage(Long senderId, Long projectId, String content) {
		User sender = userRepository.findById(senderId)
				.orElseThrow(() -> new ProjectManagementException(
						ErrorCodeEnum.USER_NOT_FOUND.getErrorCode(),
						ErrorCodeEnum.USER_NOT_FOUND.getErrorMessage(),
						HttpStatus.BAD_REQUEST));
		
		ChatDTO chatDTO = projectService.getProjectById(projectId).getChat();
		Chat chat = modelMapper.map(chatDTO, Chat.class);
		Message message = new Message();
		message.setContent(content);
		message.setSender(sender);
		message.setCreatedAt(LocalDateTime.now());
		message.setChat(chat);
		Message savedMessage = messageRepository.save(message);
		chat.getMessages().add(savedMessage);
		return modelMapper.map(savedMessage, MessageDTO.class);
	}

	@Override
	public List<MessageDTO> getMessagesByProjectId(Long projectId) {
		ChatDTO chatDTO = projectService.getChatByProjectId(projectId);
		List<Message> messages = messageRepository.findByChatIdOrderByCreatedAtAsc(chatDTO.getId());
		return messages.stream()
				.map((message) -> modelMapper.map(message, MessageDTO.class))
				.collect(Collectors.toList());
	}

}
