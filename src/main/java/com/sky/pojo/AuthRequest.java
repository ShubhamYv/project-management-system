package com.sky.pojo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthRequest {
	private String fullname;
	private String email;
	private String password;
}
