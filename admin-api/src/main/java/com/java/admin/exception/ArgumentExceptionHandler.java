package com.java.admin.exception;

import com.java.admin.constant.ApiAccountEndpoints;
import com.java.admin.constant.ApiAuthEndpoints;
import com.java.admin.constant.ApiUrlEndpoints;
import com.java.admin.constant.ApiUserEndpoints;
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

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponseDto> handleIllegalArgumentException(IllegalArgumentException ex) {
        ApiError apiError = new ApiError(
                99999, // Custom error verificationCode for IllegalArgumentException
                "Illegal Argument",
                ex.getMessage()
        );
        ApiResponseDto apiResponseDto = new ApiResponseDto(
                List.of(apiError),
                false,
                null
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponseDto);
    }

    private static Integer getFieldCode(String path, String fieldName) {
        return switch (path) {
            case String p when p.startsWith(ApiAccountEndpoints.BASE_PATH) -> getAccountCodeFieldsValidation(fieldName);
            case String p when p.startsWith(ApiAuthEndpoints.BASE_PATH) -> getAuthCodeFieldsValidation(fieldName);
            case String p when p.startsWith(ApiUrlEndpoints.BASE_PATH) -> getUrlCodeFieldsValidation(fieldName);
            case String p when p.startsWith(ApiUserEndpoints.BASE_PATH) -> getUserCodeFieldsValidation(fieldName);
            default -> 0; // Default case if no match found
        };
    }
}
