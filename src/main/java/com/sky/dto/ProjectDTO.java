package com.sky.dto;

import java.util.List;

import lombok.Data;

@Data
public class ProjectDTO {
    private Long id;
    private String name;
    private String description;
    private String category;
    private List<String> tags;
    private UserDTO owner;
    private ChatDTO chat;
    private List<IssueDTO> issues;
    private List<UserDTO> team;
}

