package com.sky.constants;

import lombok.Getter;

public enum ErrorCodeEnum {

    // General
    GENERIC_EXCEPTION("10001", "Something went wrong, please try later"),

    // User Related
    USER_NOT_FOUND("10002", "User not found with the provided details"),
    EMAIL_ALREADY_EXISTS("10003", "Email already exists with another account"),
    INVALID_CREDENTIALS("10004", "Invalid username or password"),
    INVALID_JWT("10005", "Invalid JWT token provided"),
    EMAIL_NOT_PROVIDED("10006", "Email not provided"),
    USER_ID_NOT_PROVIDED("10007", "User ID not provided"),
    USER_SAVE_FAILED("10008", "Failed to save user"),
    PROJECT_SIZE_UPDATE_FAILED("10009", "Failed to update user's project size"),

    // Issue Related
    ISSUE_NOT_FOUND("10100", "Issue not found"),
    ISSUE_SAVE_FAILED("10101", "Failed to save issue"),
    ISSUE_UPDATE_FAILED("10102", "Failed to update issue"),
    ISSUE_DELETE_FAILED("10103", "Failed to delete issue"),

    // Email Related
    EMAIL_SEND_FAILED("10200", "Failed to send email"),

    // Invitation Related
    INVITATION_SEND_FAILED("10300", "Failed to send invitation"),
    INVALID_INVITATION_TOKEN("10301", "Invalid invitation token"),
    
    // Project Related
    PROJECT_NOT_FOUND("10401", "Project not found with the provided details"),
	
    // Comment Related
    COMMENT_NOT_FOUND("10500", "Comment not found"),
    COMMENT_SAVE_FAILED("10501", "Failed to save comment"),
    COMMENT_UPDATE_FAILED("10502", "Failed to update comment"),
    COMMENT_DELETE_FAILED("10503", "User does not have permission to delete this comment!");

    @Getter
    private final String errorCode;
    @Getter
    private final String errorMessage;

    private ErrorCodeEnum(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
