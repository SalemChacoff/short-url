package com.java.admin.exception.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serial;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class UserException extends RuntimeException {

    public static final Integer USER_ACCOUNT_NOT_FOUND_CODE = 404;
    public static final Integer USER_ACCOUNT_ALREADY_EXISTS_CODE = 409;
    public static final Integer INVALID_USER_ACCOUNT_DATA_CODE = 400;
    public static final Integer DEFAULT_ERROR_CODE = 500;

    public static final String USER_ACCOUNT_NOT_FOUND_MESSAGE = "User account not found";
    public static final String USER_ACCOUNT_ALREADY_EXISTS_MESSAGE = "User account already exists";
    public static final String INVALID_USER_ACCOUNT_DATA_MESSAGE = "Invalid user account data";
    public static final String DEFAULT_ERROR_MESSAGE = "An error occurred while processing the request";

    public static final String USER_ACCOUNT_NOT_FOUND_CAUSE = "User account not found in the database";
    public static final String USER_ACCOUNT_ALREADY_EXISTS_CAUSE = "User account already exists in the database";
    public static final String INVALID_USER_ACCOUNT_DATA_CAUSE = "Invalid user account data provided";
    public static final String DEFAULT_ERROR_CAUSE = "An error occurred while processing the request";

    @Serial
    private static final long serialVersionUID = 5217213169324927321L;

    private final Integer errorCode;
    private final String errorMessage;
    private final String errorCause;
}
