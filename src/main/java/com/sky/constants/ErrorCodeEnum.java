package com.sky.constants;

import lombok.Getter;

public enum ErrorCodeEnum {

	// General
	GENERIC_EXCEPTION("10001", "Something went wrong, please try later."),

	// User Related
	USER_NOT_FOUND("10002", "User not found with the provided details."),
	EMAIL_ALREADY_EXISTS("10003", "Email already exists with another account."),
	INVALID_CREDENTIALS("10004", "Invalid username or password."), 
	USER_SAVE_FAILED("10005", "Failed to save user."),

	// Issue Related
	ISSUE_NOT_FOUND("10100", "Issue not found."), 
	ISSUE_SAVE_FAILED("10101", "Failed to save issue."),

	// Email Related
	EMAIL_SEND_FAILED("10200", "Failed to send email."),

	// Invitation Related
	INVITATION_SEND_FAILED("10300", "Failed to send invitation."),
	INVALID_INVITATION_TOKEN("10301", "Invalid invitation token."),

	// Project Related
	PROJECT_NOT_FOUND("10400", "Project not found with the provided details."),

	// Comment Related
	COMMENT_NOT_FOUND("10500", "Comment not found."),
	COMMENT_DELETE_FAILED("10501", "User does not have permission to delete this comment."),

	// Subscription Related
	SUBSCRIPTION_NOT_FOUND("10600", "Subscription not found."),

	// Razorpay Related
	PAYMENT_CREATION_FAILED("10700", "Failed to create payment link."),
	INVALID_PLAN_TYPE("10701", "Invalid plan type.");

	@Getter
	private final String errorCode;
	@Getter
	private final String errorMessage;

	private ErrorCodeEnum(String errorCode, String errorMessage) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}
}
