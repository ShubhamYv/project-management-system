package com.sky.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sky.entity.Subscription;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long>{

	Subscription findByUserId(Long userId);
}
