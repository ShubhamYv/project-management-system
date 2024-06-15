package com.sky.service;

import java.util.List;

import com.sky.dto.MessageDTO;

public interface MessageService {

	MessageDTO sendMessage(Long senderId, Long projectId, String content);

	List<MessageDTO> getMessagesByProjectId(Long projectId);
}
