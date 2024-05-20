package com.sky.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sky.entity.Chat;
import com.sky.entity.Invitation;
import com.sky.entity.Project;
import com.sky.entity.User;
import com.sky.pojo.ProjectInvitationRequest;
import com.sky.service.InvitationService;
import com.sky.service.ProjectService;
import com.sky.service.UserService;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

	private ProjectService projectService;
	private UserService userService;
	private InvitationService invitationService;

	public ProjectController(ProjectService projectService, UserService userService,
			InvitationService invitationService) {
		this.projectService = projectService;
		this.userService = userService;
		this.invitationService = invitationService;
	}
	
	@PostMapping
	public ResponseEntity<Project> createProject(@RequestBody Project project,
			@RequestHeader("Authorization") String jwt) throws Exception {
		User user = userService.findUserProfileByJwt(jwt);
		Project createdProject = projectService.createProject(project, user);
		return new ResponseEntity<>(createdProject, HttpStatus.CREATED);
	}

	@GetMapping
	public ResponseEntity<List<Project>> getProjects(
			@RequestParam(required = false) String category,
			@RequestParam(required = false) String tag,
			@RequestHeader("Authorization") String jwt) throws Exception {

		User user = userService.findUserProfileByJwt(jwt);
		List<Project> projects = projectService.getProjectByTeam(user, category, tag);
		return new ResponseEntity<>(projects, HttpStatus.OK);
	}

	@GetMapping("{projectId}")
	public ResponseEntity<Project> getProjectById(@PathVariable Long projectId,
			@RequestHeader("Authorization") String jwt) throws Exception {
		User user = userService.findUserProfileByJwt(jwt);
		Project project = projectService.getProjectById(projectId);
		return new ResponseEntity<>(project, HttpStatus.OK);
	}

	@PutMapping("/{projectId}")
	public ResponseEntity<Project> updateProject(
			@RequestBody Project project,
			@PathVariable Long projectId,
			@RequestHeader("Authorization") String jwt) throws Exception {
		User user = userService.findUserProfileByJwt(jwt);
		Project updatedProject = projectService.updateProject(project, projectId);
		return new ResponseEntity<>(updatedProject, HttpStatus.CREATED);
	}

	@DeleteMapping("{projectId}")
	public ResponseEntity<String> deleteProject(@PathVariable Long projectId,
			@RequestHeader("Authorization") String jwt) throws Exception {
		User user = userService.findUserProfileByJwt(jwt);
		projectService.deleteProject(projectId, user.getId());
		return new ResponseEntity<>("Project " + projectId + " deleted successfull", HttpStatus.OK);
	}

	@GetMapping("/search")
	public ResponseEntity<List<Project>> searchProjects(
			@RequestParam(required = false) String keyword,
			@RequestHeader("Authorization") String jwt) throws Exception {
		User user = userService.findUserProfileByJwt(jwt);
		List<Project> projects = projectService.searchProject(keyword, user);
		return new ResponseEntity<>(projects, HttpStatus.OK);
	}

	@GetMapping("/{projectId}/chat")
	public ResponseEntity<Chat> getChatProjectId(
			@PathVariable Long projectId,
			@RequestHeader("Authorization") String jwt) throws Exception {
		User user = userService.findUserProfileByJwt(jwt);
		Chat chat = projectService.getChatByProjectId(projectId);
		return new ResponseEntity<>(chat, HttpStatus.OK);
	}
	
	@PostMapping("/invite")
	public ResponseEntity<String> sendProjectInvitation(
			@RequestBody ProjectInvitationRequest request,
			@RequestHeader("Authorization") String jwt) throws Exception {
		User user = userService.findUserProfileByJwt(jwt);
		invitationService.sendInvitation(request.getEmail(), request.getProjectId());
		return new ResponseEntity<>("Project invitation sent", HttpStatus.OK);
	}
	
	@GetMapping("/accept_invitation")
	public ResponseEntity<Invitation> acceptProjectInvitation(
			@RequestBody ProjectInvitationRequest request,
			@RequestParam String token, 
			@RequestHeader("Authorization") String jwt) throws Exception {
		User user = userService.findUserProfileByJwt(jwt);
		Invitation invitation = invitationService.acceptInvitation(token, user.getId());
		projectService.addUserToProject(invitation.getProjectId(), user.getId());
		return new ResponseEntity<>(invitation, HttpStatus.ACCEPTED);
	}
}
