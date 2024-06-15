package com.sky.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.sky.constants.ErrorCodeEnum;
import com.sky.constants.PaymentConstants;
import com.sky.dto.UserDTO;
import com.sky.entity.PlanType;
import com.sky.exception.ProjectManagementException;
import com.sky.pojo.PaymentLinkResponse;
import com.sky.service.UserService;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Value("${razorpay.api.key}")
    private String apiKey;

    @Value("${razorpay.api.secret}")
    private String apiSecret;

    private final UserService userService;

    public PaymentController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/{planType}")
    public ResponseEntity<PaymentLinkResponse> createPaymentLink(@PathVariable PlanType planType,
            @RequestHeader("Authorization") String jwt) {

        UserDTO userDTO = userService.findUserProfileByJwt(jwt);

        int amount = PaymentConstants.DEFAULT_AMOUNT;
        if (planType.equals(PlanType.ANNUALLY)) {
            amount = amount * PaymentConstants.MONTHS_IN_YEAR;
            amount = (int) (amount * PaymentConstants.ANNUAL_DISCOUNT_PERCENTAGE);
        } else if (!planType.equals(PlanType.MONTHLY)) {
            throw new ProjectManagementException(
                    ErrorCodeEnum.INVALID_PLAN_TYPE.getErrorCode(),
                    ErrorCodeEnum.INVALID_PLAN_TYPE.getErrorMessage(),
                    HttpStatus.BAD_REQUEST);
        }

        try {
            RazorpayClient razorpayClient = new RazorpayClient(apiKey, apiSecret);
            JSONObject paymentLinkRequest = new JSONObject();
            paymentLinkRequest.put("amount", amount);
            paymentLinkRequest.put("currency", PaymentConstants.CURRENCY);

            JSONObject customer = new JSONObject();
            customer.put("name", userDTO.getFullname());
            customer.put("email", userDTO.getEmail());

            paymentLinkRequest.put("customer", customer);

            JSONObject notify = new JSONObject();
            notify.put("email", true);
            paymentLinkRequest.put("notify", notify);

            String callbackUrl = PaymentConstants.CALLBACK_URL_BASE + planType;
            paymentLinkRequest.put("callback_url", callbackUrl);

            PaymentLink payment = razorpayClient.paymentLink.create(paymentLinkRequest);
            String paymentLinkId = payment.get("id");
            String paymentLinkUrl = payment.get("short_url");

            PaymentLinkResponse paymentLinkResponse = new PaymentLinkResponse();
            paymentLinkResponse.setPayment_link_id(paymentLinkId);
            paymentLinkResponse.setPayment_link_url(paymentLinkUrl);

            return new ResponseEntity<>(paymentLinkResponse, HttpStatus.CREATED);
        } catch (RazorpayException e) {
            throw new ProjectManagementException(
                    ErrorCodeEnum.PAYMENT_CREATION_FAILED.getErrorCode(),
                    ErrorCodeEnum.PAYMENT_CREATION_FAILED.getErrorMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
