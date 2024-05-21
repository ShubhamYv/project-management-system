package com.sky.service;

import com.sky.dto.UserDTO;

public interface UserService {

	UserDTO findUserProfileByJwt(String jwt) throws Exception;
	
	UserDTO findUserByEmail(String email) throws Exception;
	
	UserDTO findUserById(Long userId) throws Exception;
	
	UserDTO updateUsersProjectSize(UserDTO userDTO, int number);
}
