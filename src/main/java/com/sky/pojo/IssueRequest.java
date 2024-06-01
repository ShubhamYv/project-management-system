package com.sky.pojo;

import java.time.LocalDate;
import java.util.List;

import com.sky.dto.UserDTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IssueRequest {
    private String title;
    private String description;
    private String status;
    private Long projectID;
    private String priority;
    private LocalDate dueDate;
    private List<String> tags;
    private UserDTO assignee;
}
