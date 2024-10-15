package com.eihabitat.eihabitat_server.exception;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    USER_EXISTED( 1001, "User already exists", HttpStatus.INTERNAL_SERVER_ERROR ),
    UNCATEGORIZED_EXCEPTION( 9999, "Uncategorized exception", HttpStatus.BAD_REQUEST),
    USER_PASSWORD_INVALID( 1002, "User password must be at least 8 characters", HttpStatus.BAD_REQUEST),
    INVALID_KEY( 1000, "Invalid message key", HttpStatus.BAD_REQUEST),
    PROFILE_NAME_INVALID( 1003, "Profile name must be at least 6 characters", HttpStatus.BAD_REQUEST),
    EMAIL_INVALID( 1004, "Email invalid", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND( 1005, "User not found", HttpStatus.NOT_FOUND),
    USER_NOT_EXISTED( 1006, "User not exists", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED( 1007, "User unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED( 1008, "User does not have permission", HttpStatus.FORBIDDEN),
    POST_NOT_FOUND( 1009, "Post not found", HttpStatus.NOT_FOUND),
    STORY_NOT_FOUND( 1010, "Story not found", HttpStatus.NOT_FOUND),
    COMMENT_NOT_EXISTED(1011, "Comment not exists", HttpStatus.NOT_FOUND),
    USERNAME_EXISTED(1012, "Username already exists", HttpStatus.CONFLICT),
    FOLLOW_RELATIONSHIP_NOT_FOUND(1013, "Follow relationship not found", HttpStatus.NOT_FOUND),
    TOKEN_NOT_VALID(1014, "Token not valid", HttpStatus.UNAUTHORIZED),
    ;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private int code;
    private String message;
    private HttpStatusCode statusCode;
}
