package com.sky.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class IssueDTO {
    private Long id;
    private String title;
    private String description;
    private String status;
    private Long projectID;
    private String priority;
    private LocalDate dueDate;
    private List<String> tags;
    private UserDTO assignee;
}
