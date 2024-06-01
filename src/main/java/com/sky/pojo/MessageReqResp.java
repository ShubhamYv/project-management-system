package com.sky.pojo;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MessageReqResp {

    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private Long chatId;
    private Long senderId;
}
