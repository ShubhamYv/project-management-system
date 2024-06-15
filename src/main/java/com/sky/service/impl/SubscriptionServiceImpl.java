package com.sky.service.impl;

import java.time.LocalDate;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.sky.constants.ErrorCodeEnum;
import com.sky.dto.SubscriptionDTO;
import com.sky.dto.UserDTO;
import com.sky.entity.PlanType;
import com.sky.entity.Subscription;
import com.sky.entity.User;
import com.sky.exception.ProjectManagementException;
import com.sky.repository.SubscriptionRepository;
import com.sky.service.SubscriptionService;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final ModelMapper modelMapper;

    public SubscriptionServiceImpl(SubscriptionRepository subscriptionRepository, ModelMapper modelMapper) {
        this.subscriptionRepository = subscriptionRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public SubscriptionDTO createSubscription(UserDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);
        Subscription subscription = new Subscription();
        subscription.setUser(user);
        subscription.setSubscriptionStartDate(LocalDate.now());
        subscription.setSubscriptionEndDate(LocalDate.now().plusMonths(12));
        subscription.setValid(true);
        subscription.setPlanType(PlanType.FREE);
        Subscription savedSubscription = subscriptionRepository.save(subscription);
        return modelMapper.map(savedSubscription, SubscriptionDTO.class);
    }

    @Override
    public SubscriptionDTO getUsersSubscription(Long userId) {
        Subscription subscription = subscriptionRepository.findByUserId(userId);
        if (subscription == null) {
            throw new ProjectManagementException(
            		ErrorCodeEnum.SUBSCRIPTION_NOT_FOUND.getErrorCode(), 
                    ErrorCodeEnum.SUBSCRIPTION_NOT_FOUND.getErrorMessage(), 
                    HttpStatus.NOT_FOUND);
        }
        if (!isValid(subscription)) {
            subscription.setPlanType(PlanType.FREE);
            subscription.setSubscriptionEndDate(LocalDate.now().plusMonths(12));
            subscription.setSubscriptionStartDate(LocalDate.now());
            subscription = subscriptionRepository.save(subscription);
        }
        return modelMapper.map(subscription, SubscriptionDTO.class);
    }

    @Override
    public SubscriptionDTO upgradeSubscription(Long userId, PlanType planType) {
        Subscription subscription = subscriptionRepository.findByUserId(userId);
        if (subscription == null) {
            throw new ProjectManagementException(
            		ErrorCodeEnum.SUBSCRIPTION_NOT_FOUND.getErrorCode(), 
                    ErrorCodeEnum.SUBSCRIPTION_NOT_FOUND.getErrorMessage(), 
                    HttpStatus.NOT_FOUND);
        }
        subscription.setPlanType(planType);
        subscription.setSubscriptionStartDate(LocalDate.now());

        if (planType.equals(PlanType.ANNUALLY)) {
            subscription.setSubscriptionEndDate(LocalDate.now().plusMonths(12));
        } else {
            subscription.setSubscriptionEndDate(LocalDate.now().plusMonths(1));
        }
        Subscription savedSubscription = subscriptionRepository.save(subscription);
        return modelMapper.map(savedSubscription, SubscriptionDTO.class);
    }

    @Override
    public boolean isValid(Subscription subscription) {
        if (subscription.getPlanType().equals(PlanType.FREE)) {
            return true;
        }
        LocalDate endDate = subscription.getSubscriptionEndDate();
        LocalDate currentDate = LocalDate.now();
        return endDate.isAfter(currentDate) || endDate.isEqual(currentDate); 
    }
}
