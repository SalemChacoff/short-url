package com.java.admin.exception.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class AccountException extends RuntimeException {

    public static final Integer USER_ACCOUNT_NOT_FOUND_CODE = 404;
    public static final Integer USER_ACCOUNT_ALREADY_EXISTS_CODE = 409;
    public static final Integer INVALID_USER_ACCOUNT_DATA_CODE = 400;
    public static final Integer DEFAULT_ERROR_CODE = 500;
    public static final Integer VERIFICATION_TOKEN_EXPIRED_CODE = 410;
    public static final Integer USER_ACCOUNT_ALREADY_ENABLED_CODE = 409;
    public static final Integer MAX_VERIFICATION_CODE_ATTEMPTS_REACHED_CODE = 429;
    public static final Integer INVALID_VERIFICATION_CODE_OR_PASSWORD_CODE = 401;
    public static final Integer RESET_PASSWORD_TOKEN_EXPIRED_CODE = 410;
    public static final Integer MAX_RESET_PASSWORD_ATTEMPTS_REACHED_CODE = 429;
    public static final Integer INVALID_RESET_PASSWORD_CODE_OR_CURRENT_PASSWORD_CODE = 400;
    public static final Integer USER_ACCOUNT_NOT_ENABLED_CODE = 403;

    public static final String USER_ACCOUNT_NOT_FOUND_MESSAGE = "User account not found";
    public static final String USER_ACCOUNT_ALREADY_EXISTS_MESSAGE = "User account already exists";
    public static final String INVALID_USER_ACCOUNT_DATA_MESSAGE = "Invalid user account data";
    public static final String DEFAULT_ERROR_MESSAGE = "An error occurred while processing the request";
    public static final String VERIFICATION_TOKEN_EXPIRED_MESSAGE = "Verification token has expired";
    public static final String USER_ACCOUNT_ALREADY_ENABLED_MESSAGE = "User account is already enabled";
    public static final String MAX_VERIFICATION_CODE_ATTEMPTS_REACHED_MESSAGE = "Maximum verification verificationCode attempts reached";
    public static final String INVALID_VERIFICATION_CODE_OR_PASSWORD_MESSAGE = "Invalid verification verificationCode or password";
    public static final String RESET_PASSWORD_TOKEN_EXPIRED_MESSAGE = "Reset password token has expired";
    public static final String MAX_RESET_PASSWORD_ATTEMPTS_REACHED_MESSAGE = "Maximum reset password attempts reached";
    public static final String INVALID_RESET_PASSWORD_CODE_OR_CURRENT_PASSWORD_MESSAGE = "Invalid reset password verificationCode or current password";
    public static final String USER_ACCOUNT_NOT_ENABLED_MESSAGE = "User account is not enabled";

    public static final String USER_ACCOUNT_NOT_FOUND_CAUSE = "UserNotFoundException";
    public static final String USER_ACCOUNT_ALREADY_EXISTS_CAUSE = "UserAccountAlreadyExistsException";
    public static final String INVALID_USER_ACCOUNT_DATA_CAUSE = "InvalidUserAccountDataException";
    public static final String DEFAULT_ERROR_CAUSE = "GeneralAccountException";
    public static final String VERIFICATION_TOKEN_EXPIRED_CAUSE = "VerificationTokenExpiredException";
    public static final String USER_ACCOUNT_ALREADY_ENABLED_CAUSE = "UserAccountAlreadyEnabledException";
    public static final String MAX_VERIFICATION_CODE_ATTEMPTS_REACHED_CAUSE = "MaximumVerificationCodeAttemptsReachedException";
    public static final String INVALID_VERIFICATION_CODE_OR_PASSWORD_CAUSE = "InvalidVerificationCodeOrPasswordException";
    public static final String RESET_PASSWORD_TOKEN_EXPIRED_CAUSE = "ResetPasswordTokenExpiredException";
    public static final String MAX_RESET_PASSWORD_ATTEMPTS_REACHED_CAUSE = "MaximumResetPasswordAttemptsReachedException";
    public static final String INVALID_RESET_PASSWORD_CODE_OR_CURRENT_PASSWORD_CAUSE = "InvalidResetPasswordCodeOrCurrentPasswordException";
    public static final String USER_ACCOUNT_NOT_ENABLED_CAUSE = "UserAccountNotEnabledException";

    @Serial
    private static final long serialVersionUID = -5797901353042878459L;

    private final Integer errorCode;
    private final String errorMessage;
    private final String errorCause;

}
