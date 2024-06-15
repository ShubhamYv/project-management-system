package com.sky.pojo;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class MessageResponse {

    private String content;
    private LocalDateTime createdAt;
    private Long chatId;
    private Long senderId;
}
