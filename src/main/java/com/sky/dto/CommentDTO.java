package com.sky.dto;

import java.time.LocalDateTime;

import com.sky.entity.Issue;
import com.sky.entity.User;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentDTO {

	private String content;
	private LocalDateTime createdDateTime;
	private User user;
	private Issue issue;
}
