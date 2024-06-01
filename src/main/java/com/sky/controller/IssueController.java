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

import com.sky.dto.IssueDTO;
import com.sky.dto.UserDTO;
import com.sky.pojo.IssueReqResp;
import com.sky.service.IssueService;
import com.sky.service.UserService;

@RestController
@RequestMapping("/api/issues")
public class IssueController {

    private final IssueService issueService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public IssueController(IssueService issueService, UserService userService, ModelMapper modelMapper) {
        this.issueService = issueService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/{issueId}")
    public ResponseEntity<IssueReqResp> getIssueById(@PathVariable Long issueId) {
        IssueDTO issueDTO = issueService.getIssueById(issueId);
        IssueReqResp issueResponse = modelMapper.map(issueDTO, IssueReqResp.class);
        return ResponseEntity.ok(issueResponse);
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<IssueReqResp>> getIssueByProjectId(@PathVariable Long projectId) {
        List<IssueDTO> issueDTOs = issueService.getIssueByProjectId(projectId);
        List<IssueReqResp> issueResponse = issueDTOs.stream()
                .map(issueDTO -> modelMapper.map(issueDTO, IssueReqResp.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(issueResponse);
    }

    @PostMapping
    public ResponseEntity<IssueReqResp> createIssue(
    		@RequestBody IssueReqResp issueReq,
            @RequestHeader("Authorization") String token) {

        UserDTO userDTO = userService.findUserProfileByJwt(token);
        IssueDTO issueDTO = modelMapper.map(issueReq, IssueDTO.class);

        IssueDTO createdIssue = issueService.createIssue(issueDTO, userDTO);
        IssueReqResp issueResponse = modelMapper.map(createdIssue, IssueReqResp.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(issueResponse);
    }

    @PutMapping("/{issueId}")
    public ResponseEntity<IssueReqResp> updateIssue(@PathVariable Long issueId,
            @RequestBody IssueReqResp issueReq,
            @RequestHeader("Authorization") String token) {

        UserDTO userDTO = userService.findUserProfileByJwt(token);
        IssueDTO issueDTO = modelMapper.map(issueReq, IssueDTO.class);
        IssueDTO updatedIssue = issueService.updateIssue(issueId, issueDTO, userDTO.getId());
        IssueReqResp issueResponse = modelMapper.map(updatedIssue, IssueReqResp.class);
        return ResponseEntity.ok(issueResponse);
    }

    @DeleteMapping("/{issueId}")
    public ResponseEntity<String> deleteIssue(
    		@PathVariable Long issueId,
            @RequestHeader("Authorization") String token) {
    	
        UserDTO userDTO = userService.findUserProfileByJwt(token);
        issueService.deleteIssue(issueId, userDTO.getId());
        return ResponseEntity.ok("Issue id: " + issueId + " deleted successfully!");
    }

    @GetMapping("/search")
    public ResponseEntity<List<IssueReqResp>> searchIssues(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String status, 
            @RequestParam(required = false) String priority,
            @RequestParam(required = false) Long assigneeId) {

        List<IssueDTO> searchIssuesDTO = issueService.searchIssues(title, status, priority, assigneeId);
        List<IssueReqResp> searchIssuesResponse = searchIssuesDTO.stream()
                .map(issueDTO -> modelMapper.map(issueDTO, IssueReqResp.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(searchIssuesResponse);
    }

    @PutMapping("/{issueId}/assignee/{userId}")
    public ResponseEntity<IssueReqResp> addUserToIssue(
    		@PathVariable Long issueId,
    		@PathVariable Long userId) {
    	
        IssueDTO issueDTO = issueService.addUserToIssue(issueId, userId);
        IssueReqResp issueResponse = modelMapper.map(issueDTO, IssueReqResp.class);
        return ResponseEntity.ok(issueResponse);
    }

    @GetMapping("/assignee/{assigneeId}")
    public ResponseEntity<List<IssueReqResp>> getIssuesByAssigneeId(@PathVariable Long assigneeId) {
        List<IssueDTO> issuesDTO = issueService.getIssueByAssigneeId(assigneeId);
        List<IssueReqResp> issuesResponse = issuesDTO.stream()
                .map(issueDTO -> modelMapper.map(issueDTO, IssueReqResp.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(issuesResponse);
    }

    @PutMapping("/{issueId}/status/{status}")
    public ResponseEntity<IssueReqResp> updateIssueStatus(
    		@PathVariable Long issueId,
    		@PathVariable String status) {
    	
        IssueDTO issueDTO = issueService.updateStatus(issueId, status);
        IssueReqResp issueResponse = modelMapper.map(issueDTO, IssueReqResp.class);
        return ResponseEntity.ok(issueResponse);
    }
}
