package com.sky.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.sky.dto.IssueDTO;
import com.sky.dto.ProjectDTO;
import com.sky.dto.UserDTO;
import com.sky.entity.Issue;
import com.sky.entity.Project;
import com.sky.entity.User;
import com.sky.repository.IssueRepository;
import com.sky.service.IssueService;
import com.sky.service.ProjectService;
import com.sky.service.UserService;

@Service
public class IssueServiceImpl implements IssueService {

    private IssueRepository issueRepository;
    private ModelMapper modelMapper;
    private ProjectService projectService;
    private UserService userService;

    
    public IssueServiceImpl(IssueRepository issueRepository, ModelMapper modelMapper,
                            ProjectService projectService, UserService userService) {
        this.issueRepository = issueRepository;
        this.modelMapper = modelMapper;
        this.projectService = projectService;
        this.userService = userService;
    }

    @Override
    public IssueDTO getIssueById(Long issueId) throws Exception {
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new Exception("Issue not found with ID: " + issueId));
        return modelMapper.map(issue, IssueDTO.class);
    }

    @Override
    public List<IssueDTO> getIssueByProjectId(Long projectId) throws Exception {
        List<Issue> allIssues = issueRepository.findByProjectID(projectId);
        return allIssues.stream()
                .map(issue -> modelMapper.map(issue, IssueDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public IssueDTO createIssue(IssueDTO issueDTO, UserDTO userDTO) throws Exception {
        ProjectDTO projectDTO = projectService.getProjectById(issueDTO.getProjectID());
        Project project = modelMapper.map(projectDTO, Project.class);
        Issue issue = modelMapper.map(issueDTO, Issue.class);
        issue.setProject(project);
        Issue createdIssue = issueRepository.save(issue);
        return modelMapper.map(createdIssue, IssueDTO.class);
    }

    @Override
    public IssueDTO updateIssue(Long issueId, IssueDTO issueDTO, Long userId) throws Exception {
        Issue existingIssue = issueRepository.findById(issueId)
                .orElseThrow(() -> new Exception("Issue not found with ID: " + issueId));

        // Update fields from DTO
        existingIssue.setTitle(issueDTO.getTitle());
        existingIssue.setDescription(issueDTO.getDescription());
        existingIssue.setStatus(issueDTO.getStatus());
        existingIssue.setPriority(issueDTO.getPriority());
        existingIssue.setDueDate(issueDTO.getDueDate());
        existingIssue.setTags(issueDTO.getTags());

        // Save and return updated issue
        Issue updatedIssue = issueRepository.save(existingIssue);
        return modelMapper.map(updatedIssue, IssueDTO.class);
    }

    @Override
    public void deleteIssue(Long issueId, Long userId) throws Exception {
        IssueDTO issueDTO = getIssueById(issueId);
        Issue issue = modelMapper.map(issueDTO, Issue.class);
        issueRepository.delete(issue);
    }

    @Override
    public List<IssueDTO> getIssueByAssigneeId(Long assigneeId) throws Exception {
        List<Issue> issues = issueRepository.findByAssigneeId(assigneeId);
        return issues.stream()
                .map(issue -> modelMapper.map(issue, IssueDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<IssueDTO> searchIssues(String title, String status, String priority,
                                       Long assigneeId) throws Exception {
        List<Issue> issues = issueRepository.search(title, status, priority, assigneeId);
        return issues.stream()
                .map(issue -> modelMapper.map(issue, IssueDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> getAssigneeForIssue(Long issueId) throws Exception {
        List<User> assignees = issueRepository.findAssigneesByIssueId(issueId);
        return assignees.stream()
                .map(assignee -> modelMapper.map(assignee, UserDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public IssueDTO addUserToIssue(Long issueId, Long userId) throws Exception {
        UserDTO userDTO = userService.findUserById(userId);
        IssueDTO issueDTO = getIssueById(issueId);
        Issue issue = modelMapper.map(issueDTO, Issue.class);
        User user = modelMapper.map(userDTO, User.class);
        issue.setAssignee(user);
        Issue savedIssue = issueRepository.save(issue);
        return modelMapper.map(savedIssue, IssueDTO.class);
    }

    @Override
    public IssueDTO updateStatus(Long issueId, String status) throws Exception {
        IssueDTO issueDTO = getIssueById(issueId);
        Issue issue = modelMapper.map(issueDTO, Issue.class);
        issue.setStatus(status);
        Issue savedIssue = issueRepository.save(issue);
        return modelMapper.map(savedIssue, IssueDTO.class);
    }
}
