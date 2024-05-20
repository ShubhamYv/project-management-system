package com.sky.pojo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProjectInvitationRequest {

	private Long projectId;
	private String email;
}
