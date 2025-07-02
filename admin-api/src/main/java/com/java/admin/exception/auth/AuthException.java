package com.java.admin.exception.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class AuthException extends RuntimeException {

    public static final Integer AUTHENTICATION_FAILED_CODE = 401;
    public static final Integer INVALID_TOKEN_CODE = 403;
    public static final Integer REFRESH_TOKEN_INVALID_CODE = 403;

    public static final String AUTHENTICATION_FAILED_MESSAGE = "Authentication failed";
    public static final String INVALID_TOKEN_MESSAGE = "Invalid or expired token";
    public static final String REFRESH_TOKEN_INVALID_MESSAGE = "Refresh token is invalid or expired";

    public static final String AUTHENTICATION_FAILED_CAUSE = "InvalidCredentialsException";
    public static final String INVALID_TOKEN_CAUSE = "InvalidTokenException";
    public static final String REFRESH_TOKEN_INVALID_CAUSE = "RefreshTokenInvalidException";

    @Serial
    private static final long serialVersionUID = 2023350088318803885L;

    private final Integer errorCode;
    private final String errorMessage;
    private final String errorCause;
}
