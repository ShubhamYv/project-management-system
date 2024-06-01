package com.sky.pojo;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class ChatReqResp {

    private String name;
    private List<MessageReqResp> messages = new ArrayList<>();
}
