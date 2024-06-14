package com.sky.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.sky.constants.ErrorCodeEnum;
import com.sky.dto.CommentDTO;
import com.sky.entity.Comment;
import com.sky.entity.Issue;
import com.sky.entity.User;
import com.sky.exception.ProjectManagementException;
import com.sky.repository.CommentRepository;
import com.sky.repository.IssueRepository;
import com.sky.repository.UserRepository;
import com.sky.service.CommentService;

@Service
public class CommentServiceImpl implements CommentService {
	
	private IssueRepository issueRepository;
	private CommentRepository commentRepository;
	private UserRepository userRepository;
	private ModelMapper modelMapper;
	
	public CommentServiceImpl(IssueRepository issueRepository, CommentRepository commentRepository, 
			UserRepository userRepository, ModelMapper modelMapper) {
		this.issueRepository = issueRepository;
		this.commentRepository = commentRepository;
		this.userRepository = userRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	public CommentDTO createComment(Long issueId, Long userId, String content) {
		Issue issue = issueRepository.findById(issueId)
				.orElseThrow(() -> new ProjectManagementException(
						ErrorCodeEnum.ISSUE_NOT_FOUND.getErrorCode(),
						ErrorCodeEnum.ISSUE_NOT_FOUND.getErrorMessage(), 
						HttpStatus.BAD_REQUEST));
		
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ProjectManagementException(
						ErrorCodeEnum.USER_NOT_FOUND.getErrorCode(),
						ErrorCodeEnum.USER_NOT_FOUND.getErrorMessage(), 
						HttpStatus.BAD_REQUEST));
		
		Comment comment= new Comment();
		comment.setContent(content);
		comment.setIssue(issue);
		comment.setUser(user);
		comment.setCreatedDateTime(LocalDateTime.now());
		
		Comment savedComment = commentRepository.save(comment);
		issue.getComments().add(savedComment);
		return modelMapper.map(savedComment, CommentDTO.class);
	}

	@Override
	public void deleteComment(Long commentId, Long userId) {
		Comment comment = commentRepository.findById(commentId)
				.orElseThrow(() -> new ProjectManagementException(
						ErrorCodeEnum.COMMENT_NOT_FOUND.getErrorCode(), 
						ErrorCodeEnum.COMMENT_NOT_FOUND.getErrorMessage(), 
						HttpStatus.BAD_REQUEST));
		
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ProjectManagementException(
						ErrorCodeEnum.USER_NOT_FOUND.getErrorCode(),
						ErrorCodeEnum.USER_NOT_FOUND.getErrorMessage(), 
						HttpStatus.BAD_REQUEST));
		
		if (comment.getUser().equals(user)) {
			commentRepository.delete(comment);
		} else {
			throw new ProjectManagementException(
					ErrorCodeEnum.COMMENT_DELETE_FAILED.getErrorCode(),
					ErrorCodeEnum.COMMENT_DELETE_FAILED.getErrorMessage(),
					HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	public List<CommentDTO> findCommentByIssueId(Long issueId) {
		List<Comment> comments = commentRepository.findByIssueId(issueId);
		return comments.stream()
				.map((comment) -> modelMapper.map(comment, CommentDTO.class))
				.collect(Collectors.toList());
	}

}
