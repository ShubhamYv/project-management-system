package com.sky.controller;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sky.config.JwtProvider;
import com.sky.constants.ErrorCodeEnum;
import com.sky.dto.UserDTO;
import com.sky.entity.User;
import com.sky.exception.ProjectManagementException;
import com.sky.pojo.AuthRequest;
import com.sky.pojo.AuthResponse;
import com.sky.pojo.LoginRequest;
import com.sky.repository.UserRepository;
import com.sky.service.CustomUserService;
import com.sky.service.SubscriptionService;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final CustomUserService customUserService;
	private final SubscriptionService subscriptionService;
	private final ModelMapper modelMapper;

	public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder,
			CustomUserService customUserService, SubscriptionService subscriptionService,
			ModelMapper modelMapper) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.customUserService = customUserService;
		this.subscriptionService = subscriptionService;
		this.modelMapper = modelMapper;
	}

	@PostMapping("/signup")
	public ResponseEntity<AuthResponse> createUserHandler(@RequestBody AuthRequest request) {
		User isUserExist = userRepository.findByEmail(request.getEmail());

		if (isUserExist != null) {
			System.out.println("Email already exists: " + request.getEmail());
			throw new ProjectManagementException(
						ErrorCodeEnum.EMAIL_ALREADY_EXISTS.getErrorMessage(),
						ErrorCodeEnum.EMAIL_ALREADY_EXISTS.getErrorCode(),
						HttpStatus.BAD_REQUEST
					);
		}

		User createdUser = new User();
		createdUser.setEmail(request.getEmail());
		createdUser.setFullname(request.getFullname());
		createdUser.setPassword(passwordEncoder.encode(request.getPassword()));

		User savedUser = userRepository.save(createdUser);
		UserDTO userDTO = modelMapper.map(savedUser, UserDTO.class);
		
		subscriptionService.createSubscription(userDTO);
		
		Authentication authentication = new UsernamePasswordAuthenticationToken(
					savedUser.getEmail(),
					savedUser.getPassword()
				);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		String jwt = JwtProvider.generateToken(authentication);

		AuthResponse authResponse = AuthResponse.builder()
				.jwt(jwt)
				.message("Signup Successful")
				.build();
		return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
	}

	@PostMapping("/signin")
	public ResponseEntity<AuthResponse> signin(@RequestBody LoginRequest loginRequest) {
		String username = loginRequest.getEmail();
		String password = loginRequest.getPassword();
		Authentication authentication = authenticate(username, password);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		String jwt = JwtProvider.generateToken(authentication);

		AuthResponse authResponse = AuthResponse.builder()
				.jwt(jwt)
				.message("SignIn Successful")
				.build();
		return new ResponseEntity<>(authResponse, HttpStatus.OK);
	}

	private Authentication authenticate(String username, String password) {
		UserDetails userDetails = customUserService.loadUserByUsername(username);
		if (userDetails == null 
				|| !passwordEncoder.matches(password, userDetails.getPassword())) {
			throw new ProjectManagementException(
						ErrorCodeEnum.INVALID_CREDENTIALS.getErrorMessage(),
						ErrorCodeEnum.INVALID_CREDENTIALS.getErrorCode(),
						HttpStatus.UNAUTHORIZED
					);
		}
		return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	}

}
