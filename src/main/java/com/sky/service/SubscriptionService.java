package com.sky.service;

import com.sky.dto.SubscriptionDTO;
import com.sky.dto.UserDTO;
import com.sky.entity.PlanType;
import com.sky.entity.Subscription;

public interface SubscriptionService {

	SubscriptionDTO createSubscription(UserDTO userDTO);

	SubscriptionDTO getUsersSubscription(Long userId);

	SubscriptionDTO upgradeSubscription(Long userId, PlanType planType);

	boolean isValid(Subscription subscription);
}
