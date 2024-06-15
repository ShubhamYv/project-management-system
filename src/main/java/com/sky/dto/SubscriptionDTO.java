package com.sky.dto;

import java.time.LocalDate;

import com.sky.entity.PlanType;
import com.sky.entity.User;

import lombok.Data;

@Data
public class SubscriptionDTO {

	private Long id;
	private LocalDate subscriptionStartDate;
	private LocalDate subscriptionEndDate;
	private PlanType planType;
	private boolean isValid;
	private User user;
}
