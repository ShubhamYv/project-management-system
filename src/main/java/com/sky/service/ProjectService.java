package com.sky.service;

import java.util.List;

import com.sky.dto.ChatDTO;
import com.sky.dto.ProjectDTO;
import com.sky.dto.UserDTO;

public interface ProjectService {

	ProjectDTO createProject(ProjectDTO projectDTO, UserDTO userDTO) throws Exception;

	List<ProjectDTO> getProjectByTeam(UserDTO userDTO, String category, String tag) throws Exception;

	ProjectDTO getProjectById(Long projectId) throws Exception;

	void deleteProject(Long projectId, Long userId) throws Exception;

	ProjectDTO updateProject(ProjectDTO updatedProjectDTO, Long projectId) throws Exception;

	void addUserToProject(Long projectId, Long userId) throws Exception;

	void removeUserFromProject(Long projectId, Long userId) throws Exception;

	ChatDTO getChatByProjectId(Long projectId) throws Exception;
	
	List<ProjectDTO> searchProject(String keyword, UserDTO userDTO) throws Exception;

}
