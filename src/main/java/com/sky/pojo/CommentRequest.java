package com.sky.pojo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentRequest {

	private Long issueId;
	private String content;
}
