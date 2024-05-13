package com.sky.pojo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
	
	private String jwt;
	private String message;
}
