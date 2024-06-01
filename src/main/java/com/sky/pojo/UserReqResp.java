package com.sky.pojo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserReqResp {
    private Long id;
    private String fullname;
    private String email;
    private int projectSize;
}
