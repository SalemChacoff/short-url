package com.java.admin.exception;

import com.java.admin.constant.ApiAccountEndpoints;
import com.java.admin.constant.ApiAuthEndpoints;
import com.java.admin.constant.ApiUrlEndpoints;
import com.java.admin.constant.ApiUserEndpoints;
import com.java.admin.constant.ConstraintsApi;
import com.java.admin.dto.ApiError;
import com.java.admin.dto.ApiResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Objects;

import static com.java.admin.constant.ApiAuthEndpoints.getAuthCodeFieldsValidation;
import static com.java.admin.constant.ApiAccountEndpoints.getAccountCodeFieldsValidation;
import static com.java.admin.constant.ApiUrlEndpoints.getUrlCodeFieldsValidation;
import static com.java.admin.constant.ApiUserEndpoints.getUserCodeFieldsValidation;

@RestControllerAdvice
public class ArgumentExceptionHandler {

    /**
     * Handles MethodArgumentNotValidException, which is thrown when validation fails for method arguments.
     *
     * @param ex      the exception that was thrown
     * @param request the HTTP request that caused the exception
     * @return a ResponseEntity containing an ApiResponseDto with error details
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDto> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {

        String path = request.getRequestURI();

        List<ApiError> apiErrors = ex.getFieldErrors().stream()
                .map(error -> new ApiError(
                        getFieldCode(path, error.getField()),
                        error.getField(),
                        Objects.requireNonNull(error.getDefaultMessage())
                ))
                .toList();

        ApiResponseDto apiResponseDto = new ApiResponseDto(
                apiErrors,
                false,
                null
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponseDto);
    }

    /**
     * Handles IllegalArgumentException, which is thrown when an illegal or inappropriate argument is passed.
     *
     * @param ex the exception that was thrown
     * @return a ResponseEntity containing an ApiResponseDto with error details
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponseDto> handleIllegalArgumentException(IllegalArgumentException ex) {
        ApiError apiError = new ApiError(
                ConstraintsApi.ILLEGAL_ARGUMENT_CODE, // Custom error verificationCode for IllegalArgumentException
                ConstraintsApi.ILLEGAL_ARGUMENT_MESSAGE,
                ex.getMessage()
        );
        ApiResponseDto apiResponseDto = new ApiResponseDto(
                List.of(apiError),
                false,
                null
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponseDto);
    }

    /**
     * Handles general exceptions that are not specifically caught by other handlers.
     *
     * @param ex the exception that was thrown
     * @return a ResponseEntity containing an ApiResponseDto with error details
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDto> handleException(Exception ex) {
        ApiError apiError = new ApiError(
                ConstraintsApi.GENERIC_ERROR_CODE, // Custom error verificationCode for general exceptions
                ConstraintsApi.GENERIC_ERROR_MESSAGE,
                ex.getMessage()
        );
        ApiResponseDto apiResponseDto = new ApiResponseDto(
                List.of(apiError),
                false,
                null
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponseDto);
    }

    /**
     * Determines the error code based on the request path and field name.
     *
     * @param path       the request path
     * @param fieldName  the name of the field that caused the validation error
     * @return the corresponding error code, or 0 if no match is found
     */
    private static Integer getFieldCode(String path, String fieldName) {
        return switch (path) {
            case String p when p.contains(ApiAccountEndpoints.BASE_PATH) -> getAccountCodeFieldsValidation(fieldName);
            case String p when p.contains(ApiAuthEndpoints.BASE_PATH) -> getAuthCodeFieldsValidation(fieldName);
            case String p when p.contains(ApiUrlEndpoints.BASE_PATH) -> getUrlCodeFieldsValidation(fieldName);
            case String p when p.contains(ApiUserEndpoints.BASE_PATH) -> getUserCodeFieldsValidation(fieldName);
            default -> 0; // Default case if no match found
        };
    }
}
