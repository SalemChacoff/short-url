package com.java.admin.exception.account;

import com.java.admin.config.CustomLogger;
import com.java.admin.dto.ApiError;
import com.java.admin.dto.ApiResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
@RequiredArgsConstructor
public class ServicesAccountExceptionHandler {

    @ExceptionHandler(AccountException.class)
    public ResponseEntity<ApiResponseDto> handleValidationException(AccountException e) {

        CustomLogger.logError(ServicesAccountExceptionHandler.class, "AccountException occurred: " + e.getMessage(), e);

        ApiResponseDto apiResponseDto = new ApiResponseDto(
                List.of(new ApiError(e.getErrorCode(), e.getErrorMessage(), e.getErrorCause())),
                false,
                null
        );

        return ResponseEntity.status(e.getErrorCode()).body(apiResponseDto);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDto> handleGeneralException(Exception e) {

        CustomLogger.logError(ServicesAccountExceptionHandler.class, "General exception occurred: " + e.getMessage(), e);

        ApiResponseDto apiResponseDto = new ApiResponseDto(
                List.of(new ApiError(500, "Internal Server Error", e.getMessage())),
                false,
                null
        );

        return ResponseEntity.status(500).body(apiResponseDto);
    }
}
