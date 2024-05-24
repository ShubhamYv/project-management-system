package com.sky.service;

import java.util.List;

import com.sky.dto.IssueDTO;
import com.sky.dto.UserDTO;

public interface IssueService {

	IssueDTO getIssueById(Long issueId) throws Exception;

	List<IssueDTO> getIssueByProjectId(Long projectId) throws Exception;

	IssueDTO createIssue(IssueDTO issueDTO, UserDTO userDTO) throws Exception;

	IssueDTO updateIssue(Long issueId, IssueDTO issueDTO, Long userId) throws Exception;

	void deleteIssue(Long issueId, Long userId) throws Exception;

	List<IssueDTO> getIssueByAssigneeId(Long assigneeId) throws Exception;

	List<IssueDTO> searchIssues(String title, String status, String priority, Long assigneeId) throws Exception;

	List<UserDTO> getAssigneeForIssue(Long issueId) throws Exception;

	IssueDTO addUserToIssue(Long issueId, Long userId) throws Exception;

	IssueDTO updateStatus(Long issueId, String status) throws Exception;
}
