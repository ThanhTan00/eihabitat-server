package com.eihabitat.eihabitat_server.exception;

import com.eihabitat.eihabitat_server.dto.request.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.nio.file.AccessDeniedException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = RuntimeException.class)
    ResponseEntity<ApiResponse> runtimeExceptionHandler(RuntimeException e) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
        apiResponse.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> runtimeExceptionHandler(AppException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
                .status(errorCode.getStatusCode())
                .body(
                        ApiResponse.builder()
                                .code(errorCode.getCode())
                                .data(errorCode.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(value = AuthorizationDeniedException.class)
    ResponseEntity<ApiResponse> accessDeniedHandler(AuthorizationDeniedException e) {
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;
        return  ResponseEntity
                .status(errorCode.getStatusCode())
                .body(
                        ApiResponse.builder()
                                .code(errorCode.getCode())
                                .data(errorCode.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        String enumkey = e.getFieldError().getDefaultMessage();
        ErrorCode errorCode = ErrorCode.INVALID_KEY;

        try {
            errorCode = ErrorCode.valueOf(enumkey);
        } catch (IllegalArgumentException e1) {

        }

        ApiResponse apiResponse = new ApiResponse();

        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());

        return ResponseEntity.badRequest().body(apiResponse);
    }
}
