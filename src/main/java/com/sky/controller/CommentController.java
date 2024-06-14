package com.sky.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sky.dto.CommentDTO;
import com.sky.dto.UserDTO;
import com.sky.pojo.CommentRequest;
import com.sky.pojo.CommentResponse;
import com.sky.service.CommentService;
import com.sky.service.UserService;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

	private CommentService commentService;
	private UserService userService;
	private ModelMapper modelMapper;

	public CommentController(CommentService commentService, UserService userService, ModelMapper modelMapper) {
		this.commentService = commentService;
		this.userService = userService;
		this.modelMapper = modelMapper;
	}
	
	@PostMapping
	public ResponseEntity<CommentResponse> createComment(
			@RequestBody CommentRequest request,
			@RequestHeader("Authorization") String jwt) {

		UserDTO userDTO = userService.findUserProfileByJwt(jwt);
		CommentDTO commentDTO = commentService.createComment(
				request.getIssueId(), 
				userDTO.getId(), 
				request.getContent());
		CommentResponse response = modelMapper.map(commentDTO, CommentResponse.class);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/{commentId}")
	public ResponseEntity<String> deleteComment(
			@PathVariable Long commentId,
			@RequestHeader("Authorization") String jwt) {

		UserDTO userDTO = userService.findUserProfileByJwt(jwt);
		commentService.deleteComment(commentId, userDTO.getId());
		return new ResponseEntity<>("Comment deleted successfully", HttpStatus.OK);
	}
	
	@DeleteMapping("/{issueId}")
	public ResponseEntity<List<CommentResponse>> getCommentByIssueId(@PathVariable Long issueId) {
		List<CommentDTO> comments = commentService.findCommentByIssueId(issueId);
		List<CommentResponse> response = comments.stream()
				.map((comment) -> modelMapper.map(comment, CommentResponse.class))
				.collect(Collectors.toList());
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
