package com.sky.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.sky.config.JwtProvider;
import com.sky.constants.ErrorCodeEnum;
import com.sky.dto.UserDTO;
import com.sky.entity.User;
import com.sky.exception.ProjectManagementException;
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
    public UserDTO findUserProfileByJwt(String jwt) {
        String email;
        try {
            email = JwtProvider.getEmailFromJwtToken(jwt);
        } catch (Exception e) {
            System.out.println("Exception in extracting email from JWT: " + e.getMessage());
            throw new ProjectManagementException(
	            		ErrorCodeEnum.INVALID_CREDENTIALS.getErrorMessage(),
	                    ErrorCodeEnum.INVALID_CREDENTIALS.getErrorCode(),
	                    HttpStatus.BAD_REQUEST
                    );
        }
        System.out.println("Extracted email from JWT: " + email);
        return findUserByEmail(email);
    }

	@Override
    public UserDTO findUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        System.out.println("User found by email: " + user);
        if (user == null) {
            System.out.println("User not found with email: " + email);
            throw new ProjectManagementException(
	            		ErrorCodeEnum.USER_NOT_FOUND.getErrorMessage(),
	                    ErrorCodeEnum.USER_NOT_FOUND.getErrorCode(),
	                    HttpStatus.BAD_REQUEST
                    );
        }
        return modelMapper.map(user, UserDTO.class);
    }

	@Override
    public UserDTO findUserById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
            new ProjectManagementException(
            		ErrorCodeEnum.USER_NOT_FOUND.getErrorMessage(),
                    ErrorCodeEnum.USER_NOT_FOUND.getErrorCode(),
                    HttpStatus.BAD_REQUEST)
        );
        System.out.println("User found by ID: " + user);
        return modelMapper.map(user, UserDTO.class);
    }

	@Override
	public UserDTO updateUsersProjectSize(UserDTO userDTO, int number) {
		userDTO.setProjectSize(userDTO.getProjectSize() + number);
		User user;
		try {
			user = modelMapper.map(userDTO, User.class);
			user = userRepository.save(user);
		} catch (Exception e) {
			System.out.println("Exception in updating user's project size: " + e.getMessage());
			throw new ProjectManagementException(
						ErrorCodeEnum.USER_SAVE_FAILED.getErrorMessage(),
						ErrorCodeEnum.USER_SAVE_FAILED.getErrorCode(),
						HttpStatus.INTERNAL_SERVER_ERROR
					);
		}
		System.out.println("User updated with new project size: " + user);
		return modelMapper.map(user, UserDTO.class);
	}
}
