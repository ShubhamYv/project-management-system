package com.sky.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.sky.constants.ErrorCodeEnum;
import com.sky.pojo.ErrorResponse;

@ControllerAdvice
public class ProjectManagementExceptionHandler {

	@ExceptionHandler(ProjectManagementException.class)
	public ResponseEntity<ErrorResponse> handleValidationException(ProjectManagementException ex) {
		System.out.println("Validation exception is-> " + ex.getErrorMessage());
		ErrorResponse errorResponse = ErrorResponse.builder()
				.errorCode(ex.getErrorCode())
				.errorMessage(ex.getErrorMessage())
				.build();
		
		System.out.println("handleValidationException response|errorResponse:" + errorResponse);
		return new ResponseEntity<>(errorResponse, ex.getHttpStatus());
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
		System.out.println("generic exception message is-> " + ex.getMessage());
		ErrorResponse errorResponse = ErrorResponse.builder()
				.errorCode(ErrorCodeEnum.GENERIC_EXCEPTION.getErrorCode())
				.errorMessage(ErrorCodeEnum.GENERIC_EXCEPTION.getErrorMessage())
				.build();
		
		System.out.println("handleGenericException response|errorResponse:" + errorResponse);
		return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
