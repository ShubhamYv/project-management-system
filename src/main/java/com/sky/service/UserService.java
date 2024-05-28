package com.sky.service;

import com.sky.dto.UserDTO;

public interface UserService {

	UserDTO findUserProfileByJwt(String jwt);
	
	UserDTO findUserByEmail(String email);
	
	UserDTO findUserById(Long userId);
	
	UserDTO updateUsersProjectSize(UserDTO userDTO, int number);

}
