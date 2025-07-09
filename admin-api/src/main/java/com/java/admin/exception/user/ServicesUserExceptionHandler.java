package com.java.admin.exception.user;

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
public class ServicesUserExceptionHandler {

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ApiResponseDto> handleValidationException(UserException e) {

        CustomLogger.logError(ServicesUserExceptionHandler.class, "UserException occurred: " + e.getMessage(), e);

        ApiResponseDto apiResponseDto = new ApiResponseDto(
                List.of(new ApiError(e.getErrorCode(), e.getErrorMessage(), e.getErrorCause())),
                false,
                null
        );

        return ResponseEntity.status(e.getErrorCode()).body(apiResponseDto);
    }

}
