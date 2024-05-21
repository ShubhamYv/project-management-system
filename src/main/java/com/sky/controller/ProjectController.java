package com.sky.controller;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sky.dto.ChatDTO;
import com.sky.dto.ProjectDTO;
import com.sky.dto.UserDTO;
import com.sky.pojo.ProjectInvitationRequest;
import com.sky.pojo.ProjectRequest;
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
    public ResponseEntity<ProjectDTO> createProject(
    		@RequestBody ProjectRequest projectRequest,
    		@RequestHeader("Authorization") String jwt) throws Exception {

        ProjectDTO projectDTO = modelMapper.map(projectRequest, ProjectDTO.class);
        UserDTO userDTO = userService.findUserProfileByJwt(jwt);
        ProjectDTO createdProjectDTO = projectService.createProject(projectDTO, userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProjectDTO);
    }

    @GetMapping
    public ResponseEntity<List<ProjectDTO>> getProjects(
    		@RequestParam(required = false) String category,
    		@RequestParam(required = false) String tag,
    		@RequestHeader("Authorization") String jwt) throws Exception {

        UserDTO userDTO = userService.findUserProfileByJwt(jwt);
        List<ProjectDTO> projectDTOs = projectService.getProjectByTeam(userDTO, category, tag);
        return ResponseEntity.ok(projectDTOs);
    }

    @GetMapping("{projectId}")
    public ResponseEntity<ProjectDTO> getProjectById(
    		@PathVariable Long projectId,
    		@RequestHeader("Authorization") String jwt) throws Exception {
        ProjectDTO projectDTO = projectService.getProjectById(projectId);
        return ResponseEntity.ok(projectDTO);
    }

    @PutMapping("/{projectId}")
    public ResponseEntity<ProjectDTO> updateProject(
    		@RequestBody ProjectDTO projectDTO,
    		@PathVariable Long projectId,
    		@RequestHeader("Authorization") String jwt) throws Exception {
        ProjectDTO updatedProjectDTO = projectService.updateProject(projectDTO, projectId);
        return ResponseEntity.ok(updatedProjectDTO);
    }

    @DeleteMapping("{projectId}")
    public ResponseEntity<String> deleteProject(
    		@PathVariable Long projectId,
    		@RequestHeader("Authorization") String jwt) throws Exception {
        UserDTO userDTO = userService.findUserProfileByJwt(jwt);
        projectService.deleteProject(projectId, userDTO.getId());
        return ResponseEntity.ok("Project " + projectId + " deleted successfully");
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProjectDTO>> searchProjects(
    		@RequestParam(required = false) String keyword,
    		@RequestHeader("Authorization") String jwt) throws Exception {
        UserDTO userDTO = userService.findUserProfileByJwt(jwt);
        List<ProjectDTO> projectDTOs = projectService.searchProject(keyword, userDTO);
        return ResponseEntity.ok(projectDTOs);
    }

    @GetMapping("/{projectId}/chat")
    public ResponseEntity<ChatDTO> getChatProjectId(
    		@PathVariable Long projectId,
    		@RequestHeader("Authorization") String jwt) throws Exception {
        ChatDTO chatDTO = projectService.getChatByProjectId(projectId);
        return ResponseEntity.ok(chatDTO);
    }

    @PostMapping("/invite")
    public ResponseEntity<String> sendProjectInvitation(
    		@RequestBody ProjectInvitationRequest request,
    		@RequestHeader("Authorization") String jwt) throws Exception {
        UserDTO userDTO = userService.findUserProfileByJwt(jwt);
        invitationService.sendInvitation(request.getEmail(), request.getProjectId());
        return ResponseEntity.ok("Project invitation sent");
    }

    @GetMapping("/accept_invitation")
    public ResponseEntity<Void> acceptProjectInvitation(
    		@RequestParam String token,
    		@RequestHeader("Authorization") String jwt) throws Exception {
        UserDTO userDTO = userService.findUserProfileByJwt(jwt);
        invitationService.acceptInvitation(token, userDTO.getId());
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
