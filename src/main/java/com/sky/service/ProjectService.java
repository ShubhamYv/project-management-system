package com.sky.service;

import java.util.List;

import com.sky.entity.Chat;
import com.sky.entity.Project;
import com.sky.entity.User;

public interface ProjectService {

	Project createProject(Project project, User user) throws Exception;

	List<Project> getProjectByTeam(User user, String category, String tag) throws Exception;

	Project getProjectById(Long projectId) throws Exception;

	void deleteProject(Long projectId, Long userId) throws Exception;

	Project updateProject(Project updatedProject, Long projectId) throws Exception;

	void addUserToProject(Long projectId, Long userId) throws Exception;

	void removeUserFromProject(Long projectId, Long userId) throws Exception;

	Chat getChatByProjectId(Long projectId) throws Exception;
	
	List<Project> searchProject(String keyword, User user) throws Exception;

}
