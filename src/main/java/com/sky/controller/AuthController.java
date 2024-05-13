package com.sky.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
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
import com.sky.entity.User;
import com.sky.pojo.AuthRequest;
import com.sky.pojo.AuthResponse;
import com.sky.pojo.LoginRequest;
import com.sky.repository.UserRepository;
import com.sky.service.CustomUserService;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private UserRepository userRepository;
	private PasswordEncoder passwordEncoder;
	private CustomUserService customUserService;

	public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder,
			CustomUserService customUserService) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.customUserService = customUserService;
	}

	@PostMapping("/signup")
	public ResponseEntity<AuthResponse> createUserHandler(@RequestBody AuthRequest request) throws Exception {
		User isUserExist = userRepository.findByEmail(request.getEmail());

		if (null != isUserExist) {
			throw new Exception("Email already exist with another account");
		}

		User createdUser = new User();
		createdUser.setEmail(request.getEmail());
		createdUser.setFullname(request.getFullname());
		createdUser.setPassword(passwordEncoder.encode(request.getPassword()));

		User savedUser = userRepository.save(createdUser);
		Authentication authentication = new UsernamePasswordAuthenticationToken(savedUser.getEmail(),
				savedUser.getPassword());
		SecurityContextHolder.getContext().setAuthentication(authentication);

		String jwt = JwtProvider.generateToken(authentication);

		AuthResponse authResponse = AuthResponse.builder()
				.jwt(jwt)
				.message("Signup Successfull")
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
				.message("SignIn Successfull")
				.build();

		return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
	}

	private Authentication authenticate(String username, String password) {
		UserDetails userDetails = customUserService.loadUserByUsername(username);
		if (null == userDetails) {
			throw new BadCredentialsException("Invalid username or password");
		}
		
		if (!passwordEncoder.matches(password, userDetails.getPassword())) {
			throw new BadCredentialsException("Invalid username or password");
		}
		
		return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	}
}
