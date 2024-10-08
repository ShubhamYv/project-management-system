package com.sky.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sky.constants.ErrorCodeEnum;
import com.sky.entity.User;
import com.sky.exception.ProjectManagementException;
import com.sky.repository.UserRepository;
import com.sky.service.CustomUserService;

@Service
public class CustomUserServiceImpl implements CustomUserService {

	private final UserRepository userRepository;

	public CustomUserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(username);

		if (null == user) {
	        throw new ProjectManagementException(
		            ErrorCodeEnum.INVALID_CREDENTIALS.getErrorMessage(),
		            ErrorCodeEnum.INVALID_CREDENTIALS.getErrorCode(),
		            HttpStatus.UNAUTHORIZED
		        );
		}

		List<GrantedAuthority> authorities = new ArrayList<>();
		return new org.springframework.security.core.userdetails.User(user.getEmail(), 
				user.getPassword(), authorities);
	}

}
