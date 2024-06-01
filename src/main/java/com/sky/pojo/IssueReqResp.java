package com.sky.pojo;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class IssueReqResp {

    private String title;
    private String description;
    private String status;
    private Long projectID;
    private String priority;
    private LocalDate dueDate;
    private List<String> tags;
    private UserReqResp assignee;
}
