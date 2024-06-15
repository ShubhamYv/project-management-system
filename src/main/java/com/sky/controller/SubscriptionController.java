package com.sky.controller;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sky.dto.SubscriptionDTO;
import com.sky.dto.UserDTO;
import com.sky.entity.PlanType;
import com.sky.pojo.SubscriptionResponse;
import com.sky.service.SubscriptionService;
import com.sky.service.UserService;

@RestController
@RequestMapping("/api/subscription")
public class SubscriptionController {

	private SubscriptionService subscriptionService;
	private UserService userService;
	private ModelMapper modelMapper;

	public SubscriptionController(SubscriptionService subscriptionService, UserService userService,
			ModelMapper modelMapper) {
		this.subscriptionService = subscriptionService;
		this.userService = userService;
		this.modelMapper = modelMapper;
	}

	@GetMapping("/user")
	public ResponseEntity<SubscriptionResponse> getUserSubscription(@RequestHeader("Authorization") String jwt) {
		UserDTO userDTO = userService.findUserProfileByJwt(jwt);
		SubscriptionDTO subscriptionDTO = subscriptionService.getUsersSubscription(userDTO.getId());
		SubscriptionResponse response = modelMapper.map(subscriptionDTO, SubscriptionResponse.class);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping("/upgrade")
	public ResponseEntity<SubscriptionResponse> upgradeSubscription(
			@RequestHeader("Authorization") String jwt,
			@RequestParam PlanType planType) {
		UserDTO userDTO = userService.findUserProfileByJwt(jwt);
		SubscriptionDTO subscriptionDTO = subscriptionService.upgradeSubscription(userDTO.getId(), planType);
		SubscriptionResponse response = modelMapper.map(subscriptionDTO, SubscriptionResponse.class);
		return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
	}
}
