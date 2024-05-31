package com.sky.service;

import java.util.List;

import com.sky.dto.IssueDTO;
import com.sky.dto.UserDTO;

public interface IssueService {

	IssueDTO getIssueById(Long issueId);

	List<IssueDTO> getIssueByProjectId(Long projectId);

	IssueDTO createIssue(IssueDTO issueDTO, UserDTO userDTO);

	IssueDTO updateIssue(Long issueId, IssueDTO issueDTO, Long userId);

	void deleteIssue(Long issueId, Long userId);

	List<IssueDTO> getIssueByAssigneeId(Long assigneeId);

	List<IssueDTO> searchIssues(String title, String status, String priority, Long assigneeId);

	List<UserDTO> getAssigneeForIssue(Long issueId);

	IssueDTO addUserToIssue(Long issueId, Long userId);

	IssueDTO updateStatus(Long issueId, String status);
}
