package com.sky.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
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

import com.sky.dto.ChatDTO;
import com.sky.dto.ProjectDTO;
import com.sky.dto.UserDTO;
import com.sky.pojo.ChatReqResp;
import com.sky.pojo.ProjectInvitationRequest;
import com.sky.pojo.ProjectReqResp;
import com.sky.service.InvitationService;
import com.sky.service.ProjectService;
import com.sky.service.UserService;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final UserService userService;
    private final InvitationService invitationService;
    private final ModelMapper modelMapper;

    public ProjectController(ProjectService projectService, UserService userService,
                             InvitationService invitationService, ModelMapper modelMapper) {
        this.projectService = projectService;
        this.userService = userService;
        this.invitationService = invitationService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    public ResponseEntity<ProjectReqResp> createProject(
    		@RequestBody ProjectReqResp projectRequest,
    		@RequestHeader("Authorization") String jwt) {

        ProjectDTO projectDTO = modelMapper.map(projectRequest, ProjectDTO.class);
        UserDTO userDTO = userService.findUserProfileByJwt(jwt);
        ProjectDTO createdProjectDTO = projectService.createProject(projectDTO, userDTO);
        ProjectReqResp resp = modelMapper.map(createdProjectDTO, ProjectReqResp.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @GetMapping
    public ResponseEntity<List<ProjectReqResp>> getProjects(
    		@RequestParam(required = false) String category,
    		@RequestParam(required = false) String tag,
    		@RequestHeader("Authorization") String jwt) {

        UserDTO userDTO = userService.findUserProfileByJwt(jwt);
        List<ProjectDTO> projectDTOs = projectService.getProjectByTeam(userDTO, category, tag);
        
        List<ProjectReqResp> projectReqResps = projectDTOs.stream()
                .map(projectDTO -> modelMapper.map(projectDTO, ProjectReqResp.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(projectReqResps);
    }

    @GetMapping("{projectId}")
    public ResponseEntity<ProjectReqResp> getProjectById(
    		@PathVariable Long projectId,
    		@RequestHeader("Authorization") String jwt) {
    	
        ProjectDTO projectDTO = projectService.getProjectById(projectId);
        ProjectReqResp response = modelMapper.map(projectDTO, ProjectReqResp.class);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{projectId}")
    public ResponseEntity<ProjectReqResp> updateProject(
    		@RequestBody ProjectReqResp projectRequest,
    		@PathVariable Long projectId,
    		@RequestHeader("Authorization") String jwt) {
    	
    	ProjectDTO projectDTO = modelMapper.map(projectRequest, ProjectDTO.class);
        ProjectDTO updatedProjectDTO = projectService.updateProject(projectDTO, projectId);
        ProjectReqResp response = modelMapper.map(updatedProjectDTO, ProjectReqResp.class);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{projectId}")
    public ResponseEntity<String> deleteProject(
    		@PathVariable Long projectId,
    		@RequestHeader("Authorization") String jwt)  {
        UserDTO userDTO = userService.findUserProfileByJwt(jwt);
        projectService.deleteProject(projectId, userDTO.getId());
        return ResponseEntity.ok("Project " + projectId + " deleted successfully");
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProjectReqResp>> searchProjects(
    		@RequestParam(required = false) String keyword,
    		@RequestHeader("Authorization") String jwt) {
    	
        UserDTO userDTO = userService.findUserProfileByJwt(jwt);
        List<ProjectDTO> projectDTOs = projectService.searchProject(keyword, userDTO);
        List<ProjectReqResp> projectResponse = projectDTOs.stream()
        		.map(projectDTO -> modelMapper.map(projectDTOs, ProjectReqResp.class))
        		.collect(Collectors.toList());
        return ResponseEntity.ok(projectResponse);
    }

    @GetMapping("/{projectId}/chat")
    public ResponseEntity<ChatReqResp> getChatProjectId(
    		@PathVariable Long projectId,
    		@RequestHeader("Authorization") String jwt) {
    	
        ChatDTO chatDTO = projectService.getChatByProjectId(projectId);
        ChatReqResp chatResponse = modelMapper.map(chatDTO, ChatReqResp.class);
        return ResponseEntity.ok(chatResponse);
    }

    @PostMapping("/invite")
    public ResponseEntity<String> sendProjectInvitation(
    		@RequestBody ProjectInvitationRequest request,
    		@RequestHeader("Authorization") String jwt) {
    	
        UserDTO userDTO = userService.findUserProfileByJwt(jwt);
        invitationService.sendInvitation(request.getEmail(), request.getProjectId());
        return ResponseEntity.ok("Project invitation sent");
    }

    @GetMapping("/accept_invitation")
    public ResponseEntity<Void> acceptProjectInvitation(
    		@RequestParam String token,
    		@RequestHeader("Authorization") String jwt) {
    	
        UserDTO userDTO = userService.findUserProfileByJwt(jwt);
        invitationService.acceptInvitation(token, userDTO.getId());
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
