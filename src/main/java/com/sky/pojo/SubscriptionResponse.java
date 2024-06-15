package com.sky.pojo;

import java.time.LocalDate;

import com.sky.entity.PlanType;
import com.sky.entity.User;

import lombok.Data;

@Data
public class SubscriptionResponse {

	private Long id;
	private LocalDate subscriptionStartDate;
	private LocalDate subscriptionEndDate;
	private PlanType planType;
	private boolean isValid;
	private User user;
}
