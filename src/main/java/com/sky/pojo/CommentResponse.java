package com.sky.pojo;

import java.time.LocalDateTime;

import com.sky.entity.Issue;
import com.sky.entity.User;

import lombok.Data;

@Data
public class CommentResponse {

	private String content;
	private LocalDateTime createdDateTime;
	private User user;
	private Issue issue;
}
