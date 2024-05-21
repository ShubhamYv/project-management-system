package com.sky.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO {

    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private Long chatId;
    private Long senderId;
}
