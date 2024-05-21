package com.sky.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.sky.dto.ChatDTO;
import com.sky.dto.ProjectDTO;
import com.sky.dto.UserDTO;
import com.sky.entity.Chat;
import com.sky.entity.Project;
import com.sky.entity.User;
import com.sky.repository.ProjectRepository;
import com.sky.service.ChatService;
import com.sky.service.ProjectService;
import com.sky.service.UserService;

@Service
public class ProjectServiceImpl implements ProjectService {

	private final ProjectRepository projectRepository;
	private final UserService userService;
	private final ChatService chatService;
	private final ModelMapper modelMapper;

	public ProjectServiceImpl(ProjectRepository projectRepository, UserService userService, ChatService chatService,
			ModelMapper modelMapper) {
		this.projectRepository = projectRepository;
		this.userService = userService;
		this.chatService = chatService;
		this.modelMapper = modelMapper;
	}

	@Override
	public ProjectDTO createProject(ProjectDTO projectDTO, UserDTO userDTO) throws Exception {
		User user = modelMapper.map(userDTO, User.class);
		UserDTO userExistDTO = userService.findUserById(user.getId());
		if (userExistDTO == null) {
			throw new IllegalArgumentException("User not found with ID: " + user.getId());
		}
		User userExist = modelMapper.map(userExistDTO, User.class);

		Project project = modelMapper.map(projectDTO, Project.class);
		project.setOwner(userExist);

		Project savedProject = projectRepository.save(project);

		// Creating a dynamic chat based on the project name
		ChatDTO chatDTO = new ChatDTO();
		chatDTO.setName(projectDTO.getName());
		ChatDTO projectChatDTO = chatService.createChat(chatDTO);
		savedProject.setChat(modelMapper.map(projectChatDTO, Chat.class));
		projectRepository.save(savedProject);

		return modelMapper.map(savedProject, ProjectDTO.class);
	}

	@Override
	public List<ProjectDTO> getProjectByTeam(UserDTO userDTO, String category, String tag) throws Exception {
		User user = modelMapper.map(userDTO, User.class);
		List<Project> projects = projectRepository.findByTeamContainingOrOwner(user, user);

		if (category != null) {
			projects = projects.stream().filter(project -> category.equals(project.getCategory()))
					.collect(Collectors.toList());
		}

		if (tag != null) {
			projects = projects.stream().filter(project -> project.getTags().contains(tag))
					.collect(Collectors.toList());
		}

		return projects.stream().map(project -> modelMapper.map(project, ProjectDTO.class))
				.collect(Collectors.toList());
	}

	@Override
	public ProjectDTO getProjectById(Long projectId) throws Exception {
		Project project = projectRepository.findById(projectId)
				.orElseThrow(() -> new Exception("Project not found with ID: " + projectId));
		return modelMapper.map(project, ProjectDTO.class);
	}

	@Override
	public void deleteProject(Long projectId, Long userId) throws Exception {
		Project project = projectRepository.findById(projectId)
				.orElseThrow(() -> new Exception("Project not found with ID: " + projectId));
		if (!project.getOwner().getId().equals(userId)) {
			throw new Exception("You are not authorized to delete this project.");
		}
		projectRepository.deleteById(projectId);
	}

	@Override
	public ProjectDTO updateProject(ProjectDTO updatedProjectDTO, Long projectId) throws Exception {
		Project project = projectRepository.findById(projectId)
				.orElseThrow(() -> new Exception("Project not found with ID: " + projectId));

		project.setCategory(updatedProjectDTO.getCategory());
		project.setName(updatedProjectDTO.getName());
		project.setDescription(updatedProjectDTO.getDescription());
		project.setTags(updatedProjectDTO.getTags());

		Project savedProject = projectRepository.save(project);
		return modelMapper.map(savedProject, ProjectDTO.class);
	}

	@Override
	public void addUserToProject(Long projectId, Long userId) throws Exception {
		Project project = projectRepository.findById(projectId)
				.orElseThrow(() -> new Exception("Project not found with ID: " + projectId));
		UserDTO userDTO = userService.findUserById(userId);
		if (userDTO == null) {
			throw new IllegalArgumentException("User not found with ID: " + userId);
		}
		User user = modelMapper.map(userDTO, User.class);
		if (!project.getTeam().contains(user)) {
			project.getTeam().add(user);
			project.getChat().getUsers().add(user);
		}
		projectRepository.save(project);
	}

	@Override
	public void removeUserFromProject(Long projectId, Long userId) throws Exception {
		Project project = projectRepository.findById(projectId)
				.orElseThrow(() -> new Exception("Project not found with ID: " + projectId));
		UserDTO userDTO = userService.findUserById(userId);
		if (userDTO == null) {
			throw new IllegalArgumentException("User not found with ID: " + userId);
		}
		User user = modelMapper.map(userDTO, User.class);
		if (project.getTeam().contains(user)) {
			project.getTeam().remove(user);
			// Remove user from chat participants
			project.getChat().getUsers().removeIf(u -> u.getId().equals(userId));
		}
		projectRepository.save(project);
	}

	@Override
	public ChatDTO getChatByProjectId(Long projectId) throws Exception {
		Project project = projectRepository.findById(projectId)
				.orElseThrow(() -> new Exception("Project not found with ID: " + projectId));
		return modelMapper.map(project.getChat(), ChatDTO.class);
	}

	@Override
	public List<ProjectDTO> searchProject(String keyword, UserDTO userDTO) throws Exception {
		String partialName = "%" + keyword + "%";
		User user = modelMapper.map(userDTO, User.class);
		List<Project> projects = projectRepository.findByNameContainingAndTeamContaining(partialName, user);
		return projects.stream().map(project -> modelMapper.map(project, ProjectDTO.class))
				.collect(Collectors.toList());
	}

}
