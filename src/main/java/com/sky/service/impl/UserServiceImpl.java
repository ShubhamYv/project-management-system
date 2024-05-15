package com.sky.service.impl;

import org.springframework.stereotype.Service;

import com.sky.config.JwtProvider;
import com.sky.entity.User;
import com.sky.repository.UserRepository;
import com.sky.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	private UserRepository userRepository;

	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public User findUserProfileByJwt(String jwt) throws Exception {
		return findUserByEmail(JwtProvider.getEmailFromJwtToken(jwt));
	}

	@Override
	public User findUserByEmail(String email) throws Exception {
		User user = userRepository.findByEmail(email);
		if (null == user) {
			throw new Exception("User not found with email: " + email);
		}
		return user;
	}

	@Override
	public User findUserById(Long userId) throws Exception {
		return userRepository.findById(userId)
				.orElseThrow(() -> new Exception("User not found with id: " + userId));
	}

	@Override
	public User updateUsersProjectSize(User user, int number) {
		user.setProjectSize(user.getProjectSize() + number);
		return userRepository.save(user);
	}

}
