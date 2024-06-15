package com.sky.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sky.dto.MessageDTO;
import com.sky.dto.ProjectDTO;
import com.sky.dto.UserDTO;
import com.sky.pojo.MessageRequest;
import com.sky.pojo.MessageResponse;
import com.sky.service.MessageService;
import com.sky.service.ProjectService;
import com.sky.service.UserService;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

	private MessageService messageService;
	private UserService userService;
	private ProjectService projectService;
	private ModelMapper modelMapper;

	public MessageController(MessageService messageService, UserService userService, ProjectService projectService,
			ModelMapper modelMapper) {
		this.messageService = messageService;
		this.userService = userService;
		this.projectService = projectService;
		this.modelMapper = modelMapper;
	}

	@PostMapping("/send")
	public ResponseEntity<MessageResponse> createMessage(@RequestBody MessageRequest request) {
		UserDTO userDTO = userService.findUserById(request.getSenderId());
		ProjectDTO projectDTO = projectService.getProjectById(request.getProjectId());
		MessageDTO messageDTO = messageService.sendMessage(
				userDTO.getId(),
				projectDTO.getId(),
				request.getContent());
		MessageResponse response = modelMapper.map(messageDTO, MessageResponse.class);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	
	@GetMapping("/chat/{projectId}")
	public ResponseEntity<List<MessageResponse>> getMessagesByProjectId(@PathVariable Long projectId) {
		List<MessageDTO> messages = messageService.getMessagesByProjectId(projectId);
		List<MessageResponse> response = messages.stream()
				.map((message) -> modelMapper.map(message, MessageResponse.class))
				.collect(Collectors.toList());
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
