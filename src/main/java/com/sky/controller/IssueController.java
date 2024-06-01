package com.sky.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sky.service.IssueService;
import com.sky.service.UserService;

@RestController
@RequestMapping("/api/issues")
public class IssueController {

	private IssueService issueService;
	private UserService userService;

	public IssueController(IssueService issueService, UserService userService) {
		this.issueService = issueService;
		this.userService = userService;
	}
	
}
