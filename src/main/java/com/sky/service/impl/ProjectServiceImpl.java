package com.sky.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.sky.entity.Chat;
import com.sky.entity.Project;
import com.sky.entity.User;
import com.sky.repository.ProjectRepository;
import com.sky.service.ChatService;
import com.sky.service.ProjectService;
import com.sky.service.UserService;

@Service
public class ProjectServiceImpl implements ProjectService {

	private ProjectRepository projectRepository;
	private UserService userService;
	private ChatService chatService;

	public ProjectServiceImpl(ProjectRepository projectRepository, UserService userService,
			ChatService chatService) {
		this.projectRepository = projectRepository;
		this.userService = userService;
		this.chatService = chatService;
	}

	@Override
	public Project createProject(Project project, User user) {
		Project createdProject = new Project();
		createdProject.setOwner(user);
		createdProject.setCategory(project.getCategory());
		createdProject.setDescription(project.getDescription());
		createdProject.setIssues(project.getIssues());
		createdProject.setName(project.getName());
		createdProject.setTags(project.getTags());
		createdProject.setTeam(project.getTeam());

		Project savedProject = projectRepository.save(createdProject);

		// Create chat associated with the project
		Chat chat = new Chat();
		chat.setProject(savedProject);
		Chat projectChat = chatService.createChat(chat);
		savedProject.setChat(projectChat);

		return savedProject;
	}

	@Override
	public List<Project> getProjectByTeam(User user, String category, String tag) {
		List<Project> projects = projectRepository.findByTeamContainsOrOwner(user, user);

		// Filter projects based on category if provided
		if (category != null) {
			projects = projects.stream().filter(project -> category.equals(project.getCategory()))
					.collect(Collectors.toList());
		}

		// Filter projects based on tag if provided
		if (tag != null) {
			projects = projects.stream().filter(project -> project.getTags().contains(tag))
					.collect(Collectors.toList());
		}

		return projects;
	}

	@Override
	public Project getProjectById(Long projectId) throws Exception {
		return projectRepository.findById(projectId)
				.orElseThrow(() -> new Exception("Project not found with id: " + projectId));
	}

	@Override
	public void deleteProject(Long projectId, Long userId) throws Exception {
		Project project = getProjectById(projectId);
		if (!project.getOwner().getId().equals(userId)) {
			throw new Exception("You are not authorized to delete this project.");
		}
		projectRepository.deleteById(projectId);
	}

	@Override
	public Project updateProject(Project updatedProject, Long projectId) throws Exception {
		Project project = getProjectById(projectId);
		project.setCategory(updatedProject.getCategory());
		project.setName(updatedProject.getName());
		project.setDescription(updatedProject.getDescription());
		project.setTags(updatedProject.getTags());
		return projectRepository.save(project);
	}

	@Override
	public void addUserToProject(Long projectId, Long userId) throws Exception {
		Project project = getProjectById(projectId);
		User user = userService.findUserById(userId);
		if (!project.getTeam().contains(user)) {
			project.getChat().getUsers().add(user);
			project.getTeam().add(user);
		}
		projectRepository.save(project);
	}

	@Override
	public void removeUserFromProject(Long projectId, Long userId) throws Exception {
		Project project = getProjectById(projectId);
		User user = userService.findUserById(userId);
		if (!project.getTeam().contains(user)) {
			project.getChat().getUsers().add(user);
			project.getTeam().remove(user);
		}
		projectRepository.save(project);
	}

	@Override
	public Chat getChatByProjectId(Long projectId) throws Exception {
		Project project = getProjectById(projectId);
		return project.getChat();
	}

	@Override
	public List<Project> searchProject(String keyword, User user) throws Exception {
		String partialName = "%" + keyword + "%";
		return projectRepository.findByNameContainsAndTeamContains(partialName, user);
	}

}
