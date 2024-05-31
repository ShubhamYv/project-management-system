package com.sky.service;

import java.util.List;

import com.sky.dto.ChatDTO;
import com.sky.dto.ProjectDTO;
import com.sky.dto.UserDTO;

public interface ProjectService {

	ProjectDTO createProject(ProjectDTO projectDTO, UserDTO userDTO);

	List<ProjectDTO> getProjectByTeam(UserDTO userDTO, String category, String tag);

	ProjectDTO getProjectById(Long projectId);

	void deleteProject(Long projectId, Long userId);

	ProjectDTO updateProject(ProjectDTO updatedProjectDTO, Long projectId);

	void addUserToProject(Long projectId, Long userId);

	void removeUserFromProject(Long projectId, Long userId);

	ChatDTO getChatByProjectId(Long projectId);

	List<ProjectDTO> searchProject(String keyword, UserDTO userDTO);

}
