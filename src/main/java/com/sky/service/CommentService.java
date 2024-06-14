package com.sky.service;

import java.util.List;

import com.sky.dto.CommentDTO;

public interface CommentService {
	
	CommentDTO createComment(Long issueId, Long userId, String content);
	
	void deleteComment(Long commentId, Long userId);
	
	List<CommentDTO> findCommentByIssueId(Long issueId);
}
