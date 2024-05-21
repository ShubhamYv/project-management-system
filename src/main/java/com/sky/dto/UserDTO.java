package com.sky.dto;

import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String fullname;
    private String email;
    private int projectSize;
}