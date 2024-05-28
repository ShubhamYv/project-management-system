package com.sky.constants;

import lombok.Getter;

public enum ErrorCodeEnum {

    GENERIC_EXCEPTION("10001", "Something went wrong, please try later"),

    // User Related
    USER_NOT_FOUND("10002", "User not found with the provided details"),
    ISSUE_NOT_FOUND("10003", "Issue not found"),
    EMAIL_ALREADY_EXISTS("10004", "Email already exists with another account"),
    INVALID_CREDENTIALS("10005", "Invalid username or password"),
    INVALID_JWT("10006", "Invalid JWT token provided"),
    EMAIL_NOT_PROVIDED("10007", "Email not provided"),
    USER_ID_NOT_PROVIDED("10008", "User ID not provided"),
    USER_SAVE_FAILED("10009", "Failed to save user"),
    PROJECT_SIZE_UPDATE_FAILED("10010", "Failed to update user's project size");

    @Getter
    private final String errorCode;
    @Getter
    private final String errorMessage;

    private ErrorCodeEnum(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
