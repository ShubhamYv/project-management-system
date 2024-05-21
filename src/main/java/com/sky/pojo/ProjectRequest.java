package com.sky.pojo;

import java.util.List;

import lombok.Data;

@Data
public class ProjectRequest {
    private String name;
    private String description;
    private String category;
    private List<String> tags;
}