package com.sky.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.sky.config.JwtProvider;
import com.sky.dto.UserDTO;
import com.sky.entity.User;
import com.sky.repository.UserRepository;
import com.sky.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final ModelMapper modelMapper;

	public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper) {
		this.userRepository = userRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	public UserDTO findUserProfileByJwt(String jwt) throws Exception {
		String email = JwtProvider.getEmailFromJwtToken(jwt);
		return findUserByEmail(email);
	}

	@Override
	public UserDTO findUserByEmail(String email) throws Exception {
		User user = userRepository.findByEmail(email);
		if (user == null) {
			throw new Exception("User not found with email: " + email);
		}
		return modelMapper.map(user, UserDTO.class);
	}

	@Override
	public UserDTO findUserById(Long userId) throws Exception {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new Exception("User not found with id: " + userId));
		return modelMapper.map(user, UserDTO.class);
	}

	@Override
	public UserDTO updateUsersProjectSize(UserDTO userDTO, int number) {
		userDTO.setProjectSize(userDTO.getProjectSize() + number);
		User user = modelMapper.map(userDTO, User.class);
		user = userRepository.save(user);
		return modelMapper.map(user, UserDTO.class);
	}
}
