package com.java.admin.exception.url;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class UrlException extends RuntimeException {

    public static final Integer URL_NOT_FOUND_CODE = 404;
    public static final Integer URL_ALREADY_EXISTS_CODE = 409;
    public static final Integer INVALID_URL_DATA_CODE = 400;
    public static final Integer DEFAULT_ERROR_CODE = 500;

    public static final String URL_NOT_FOUND_MESSAGE = "URL not found";
    public static final String URL_ALREADY_EXISTS_MESSAGE = "URL already exists";
    public static final String INVALID_URL_DATA_MESSAGE = "Invalid URL data provided";
    public static final String DEFAULT_ERROR_MESSAGE = "An error occurred while processing the request";

    public static final String URL_NOT_FOUND_CAUSE = "UrlNotFoundException";
    public static final String URL_ALREADY_EXISTS_CAUSE = "UrlAlreadyExistsException";
    public static final String INVALID_URL_DATA_CAUSE = "InvalidUrlDataException";
    public static final String DEFAULT_ERROR_CAUSE = "GeneralUrlException";

    @Serial
    private static final long serialVersionUID = -7911197321095782534L;

    private final Integer errorCode;
    private final String errorMessage;
    private final String errorCause;
}
