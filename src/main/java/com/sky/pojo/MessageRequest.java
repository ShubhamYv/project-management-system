package com.sky.pojo;

import lombok.Data;

@Data
public class MessageRequest {

	private Long senderId;
	private Long projectId;
	private String content;
}
