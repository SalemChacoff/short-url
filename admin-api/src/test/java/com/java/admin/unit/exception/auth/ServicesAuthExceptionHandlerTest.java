package com.java.admin.unit.exception.auth;

import com.java.admin.dto.ApiError;
import com.java.admin.dto.ApiResponseDto;
import com.java.admin.exception.auth.AuthException;
import com.java.admin.exception.auth.ServicesAuthExceptionHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ServicesAuthExceptionHandlerTest {

    @InjectMocks
    private ServicesAuthExceptionHandler servicesAuthExceptionHandler;

    @Test
    void handleValidationException_shouldReturnUnauthorized_whenAuthenticationFailed() {
        // Arrange
        AuthException exception = new AuthException(
                AuthException.AUTHENTICATION_FAILED_CODE,
                AuthException.AUTHENTICATION_FAILED_MESSAGE,
                AuthException.AUTHENTICATION_FAILED_CAUSE
        );

        // Act
        ResponseEntity<ApiResponseDto> response = servicesAuthExceptionHandler.handleValidationException(exception);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().success());
        assertNull(response.getBody().data());
        assertEquals(1, response.getBody().errors().size());

        ApiError error = response.getBody().errors().getFirst();
        assertEquals(AuthException.AUTHENTICATION_FAILED_CODE, error.errorCode());
        assertEquals(AuthException.AUTHENTICATION_FAILED_MESSAGE, error.errorMessage());
        assertEquals(AuthException.AUTHENTICATION_FAILED_CAUSE, error.errorCause());
    }

    @Test
    void handleValidationException_shouldReturnForbidden_whenInvalidToken() {
        // Arrange
        AuthException exception = new AuthException(
                AuthException.INVALID_TOKEN_CODE,
                AuthException.INVALID_TOKEN_MESSAGE,
                AuthException.INVALID_TOKEN_CAUSE
        );

        // Act
        ResponseEntity<ApiResponseDto> response = servicesAuthExceptionHandler.handleValidationException(exception);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().success());
        assertNull(response.getBody().data());
        assertEquals(1, response.getBody().errors().size());

        ApiError error = response.getBody().errors().getFirst();
        assertEquals(AuthException.INVALID_TOKEN_CODE, error.errorCode());
        assertEquals(AuthException.INVALID_TOKEN_MESSAGE, error.errorMessage());
        assertEquals(AuthException.INVALID_TOKEN_CAUSE, error.errorCause());
    }

    @Test
    void handleValidationException_shouldReturnForbidden_whenRefreshTokenInvalid() {
        // Arrange
        AuthException exception = new AuthException(
                AuthException.REFRESH_TOKEN_INVALID_CODE,
                AuthException.REFRESH_TOKEN_INVALID_MESSAGE,
                AuthException.REFRESH_TOKEN_INVALID_CAUSE
        );

        // Act
        ResponseEntity<ApiResponseDto> response = servicesAuthExceptionHandler.handleValidationException(exception);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().success());
        assertNull(response.getBody().data());
        assertEquals(1, response.getBody().errors().size());

        ApiError error = response.getBody().errors().getFirst();
        assertEquals(AuthException.REFRESH_TOKEN_INVALID_CODE, error.errorCode());
        assertEquals(AuthException.REFRESH_TOKEN_INVALID_MESSAGE, error.errorMessage());
        assertEquals(AuthException.REFRESH_TOKEN_INVALID_CAUSE, error.errorCause());
    }
}