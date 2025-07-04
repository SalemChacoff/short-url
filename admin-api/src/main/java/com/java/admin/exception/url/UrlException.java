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
    public static final Integer URL_UPDATE_FAILED_CODE = 422;
    public static final Integer URL_DELETE_FAILED_CODE = 422;
    public static final Integer URL_STATUS_UPDATE_FAILED_CODE = 422;

    public static final String URL_NOT_FOUND_MESSAGE = "URL not found";
    public static final String URL_ALREADY_EXISTS_MESSAGE = "URL already exists";
    public static final String URL_UPDATE_FAILED_MESSAGE = "Failed to update the URL";
    public static final String URL_DELETE_FAILED_MESSAGE = "Failed to delete the URL";
    public static final String URL_STATUS_UPDATE_FAILED_MESSAGE = "Failed to update the URL status";

    public static final String URL_NOT_FOUND_CAUSE = "UrlNotFoundException";
    public static final String URL_ALREADY_EXISTS_CAUSE = "UrlAlreadyExistsException";
    public static final String URL_UPDATE_FAILED_CAUSE = "UrlUpdateFailedException";
    public static final String URL_DELETE_FAILED_CAUSE = "UrlDeleteFailedException";
    public static final String URL_STATUS_UPDATE_FAILED_CAUSE = "UrlStatusUpdateFailedException";

    @Serial
    private static final long serialVersionUID = -7911197321095782534L;

    private final Integer errorCode;
    private final String errorMessage;
    private final String errorCause;
}
