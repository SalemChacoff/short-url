package com.java.admin.unit.exception.account;

import com.java.admin.dto.ApiError;
import com.java.admin.dto.ApiResponseDto;
import com.java.admin.exception.account.AccountException;
import com.java.admin.exception.account.ServicesAccountExceptionHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ServicesAccountExceptionHandlerTest {

    @InjectMocks
    private ServicesAccountExceptionHandler servicesAccountExceptionHandler;

    @Test
    void handleValidationException_shouldReturnNotFound_whenUserAccountNotFound() {
        // Arrange
        AccountException exception = new AccountException(
                AccountException.USER_ACCOUNT_NOT_FOUND_CODE,
                AccountException.USER_ACCOUNT_NOT_FOUND_MESSAGE,
                AccountException.USER_ACCOUNT_NOT_FOUND_CAUSE
        );

        // Act
        ResponseEntity<ApiResponseDto> response = servicesAccountExceptionHandler.handleValidationException(exception);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().success());
        assertNull(response.getBody().data());
        assertEquals(1, response.getBody().errors().size());

        ApiError error = response.getBody().errors().getFirst();
        assertEquals(AccountException.USER_ACCOUNT_NOT_FOUND_CODE, error.errorCode());
        assertEquals(AccountException.USER_ACCOUNT_NOT_FOUND_MESSAGE, error.errorMessage());
        assertEquals(AccountException.USER_ACCOUNT_NOT_FOUND_CAUSE, error.errorCause());
    }

    @Test
    void handleValidationException_shouldReturnConflict_whenUserAccountAlreadyExists() {
        // Arrange
        AccountException exception = new AccountException(
                AccountException.USER_ACCOUNT_ALREADY_EXISTS_CODE,
                AccountException.USER_ACCOUNT_ALREADY_EXISTS_MESSAGE,
                AccountException.USER_ACCOUNT_ALREADY_EXISTS_CAUSE
        );

        // Act
        ResponseEntity<ApiResponseDto> response = servicesAccountExceptionHandler.handleValidationException(exception);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().success());
        assertNull(response.getBody().data());
        assertEquals(1, response.getBody().errors().size());

        ApiError error = response.getBody().errors().getFirst();
        assertEquals(AccountException.USER_ACCOUNT_ALREADY_EXISTS_CODE, error.errorCode());
        assertEquals(AccountException.USER_ACCOUNT_ALREADY_EXISTS_MESSAGE, error.errorMessage());
        assertEquals(AccountException.USER_ACCOUNT_ALREADY_EXISTS_CAUSE, error.errorCause());
    }

    @Test
    void handleValidationException_shouldReturnBadRequest_whenInvalidUserAccountData() {
        // Arrange
        AccountException exception = new AccountException(
                AccountException.INVALID_USER_ACCOUNT_DATA_CODE,
                AccountException.INVALID_USER_ACCOUNT_DATA_MESSAGE,
                AccountException.INVALID_USER_ACCOUNT_DATA_CAUSE
        );

        // Act
        ResponseEntity<ApiResponseDto> response = servicesAccountExceptionHandler.handleValidationException(exception);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().success());
        assertNull(response.getBody().data());
        assertEquals(1, response.getBody().errors().size());

        ApiError error = response.getBody().errors().getFirst();
        assertEquals(AccountException.INVALID_USER_ACCOUNT_DATA_CODE, error.errorCode());
        assertEquals(AccountException.INVALID_USER_ACCOUNT_DATA_MESSAGE, error.errorMessage());
        assertEquals(AccountException.INVALID_USER_ACCOUNT_DATA_CAUSE, error.errorCause());
    }

    @Test
    void handleValidationException_shouldReturnUnauthorized_whenInvalidVerificationCode() {
        // Arrange
        AccountException exception = new AccountException(
                AccountException.INVALID_VERIFICATION_CODE_OR_PASSWORD_CODE,
                AccountException.INVALID_VERIFICATION_CODE_OR_PASSWORD_MESSAGE,
                AccountException.INVALID_VERIFICATION_CODE_OR_PASSWORD_CAUSE
        );

        // Act
        ResponseEntity<ApiResponseDto> response = servicesAccountExceptionHandler.handleValidationException(exception);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().success());
        assertNull(response.getBody().data());
        assertEquals(1, response.getBody().errors().size());

        ApiError error = response.getBody().errors().getFirst();
        assertEquals(AccountException.INVALID_VERIFICATION_CODE_OR_PASSWORD_CODE, error.errorCode());
        assertEquals(AccountException.INVALID_VERIFICATION_CODE_OR_PASSWORD_MESSAGE, error.errorMessage());
        assertEquals(AccountException.INVALID_VERIFICATION_CODE_OR_PASSWORD_CAUSE, error.errorCause());
    }

    @Test
    void handleValidationException_shouldReturnForbidden_whenUserAccountNotEnabled() {
        // Arrange
        AccountException exception = new AccountException(
                AccountException.USER_ACCOUNT_NOT_ENABLED_CODE,
                AccountException.USER_ACCOUNT_NOT_ENABLED_MESSAGE,
                AccountException.USER_ACCOUNT_NOT_ENABLED_CAUSE
        );

        // Act
        ResponseEntity<ApiResponseDto> response = servicesAccountExceptionHandler.handleValidationException(exception);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().success());
        assertNull(response.getBody().data());
        assertEquals(1, response.getBody().errors().size());

        ApiError error = response.getBody().errors().getFirst();
        assertEquals(AccountException.USER_ACCOUNT_NOT_ENABLED_CODE, error.errorCode());
        assertEquals(AccountException.USER_ACCOUNT_NOT_ENABLED_MESSAGE, error.errorMessage());
        assertEquals(AccountException.USER_ACCOUNT_NOT_ENABLED_CAUSE, error.errorCause());
    }

    @Test
    void handleValidationException_shouldReturnGone_whenVerificationTokenExpired() {
        // Arrange
        AccountException exception = new AccountException(
                AccountException.VERIFICATION_TOKEN_EXPIRED_CODE,
                AccountException.VERIFICATION_TOKEN_EXPIRED_MESSAGE,
                AccountException.VERIFICATION_TOKEN_EXPIRED_CAUSE
        );

        // Act
        ResponseEntity<ApiResponseDto> response = servicesAccountExceptionHandler.handleValidationException(exception);

        // Assert
        assertEquals(HttpStatus.GONE, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().success());
        assertNull(response.getBody().data());
        assertEquals(1, response.getBody().errors().size());

        ApiError error = response.getBody().errors().getFirst();
        assertEquals(AccountException.VERIFICATION_TOKEN_EXPIRED_CODE, error.errorCode());
        assertEquals(AccountException.VERIFICATION_TOKEN_EXPIRED_MESSAGE, error.errorMessage());
        assertEquals(AccountException.VERIFICATION_TOKEN_EXPIRED_CAUSE, error.errorCause());
    }

    @Test
    void handleValidationException_shouldReturnTooManyRequests_whenMaxAttemptsReached() {
        // Arrange
        AccountException exception = new AccountException(
                AccountException.MAX_VERIFICATION_CODE_ATTEMPTS_REACHED_CODE,
                AccountException.MAX_VERIFICATION_CODE_ATTEMPTS_REACHED_MESSAGE,
                AccountException.MAX_VERIFICATION_CODE_ATTEMPTS_REACHED_CAUSE
        );

        // Act
        ResponseEntity<ApiResponseDto> response = servicesAccountExceptionHandler.handleValidationException(exception);

        // Assert
        assertEquals(HttpStatus.TOO_MANY_REQUESTS, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().success());
        assertNull(response.getBody().data());
        assertEquals(1, response.getBody().errors().size());

        ApiError error = response.getBody().errors().getFirst();
        assertEquals(AccountException.MAX_VERIFICATION_CODE_ATTEMPTS_REACHED_CODE, error.errorCode());
        assertEquals(AccountException.MAX_VERIFICATION_CODE_ATTEMPTS_REACHED_MESSAGE, error.errorMessage());
        assertEquals(AccountException.MAX_VERIFICATION_CODE_ATTEMPTS_REACHED_CAUSE, error.errorCause());
    }

    @Test
    void handleValidationException_shouldReturnInternalServerError_whenDefaultError() {
        // Arrange
        AccountException exception = new AccountException(
                AccountException.DEFAULT_ERROR_CODE,
                AccountException.DEFAULT_ERROR_MESSAGE,
                AccountException.DEFAULT_ERROR_CAUSE
        );

        // Act
        ResponseEntity<ApiResponseDto> response = servicesAccountExceptionHandler.handleValidationException(exception);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().success());
        assertNull(response.getBody().data());
        assertEquals(1, response.getBody().errors().size());

        ApiError error = response.getBody().errors().getFirst();
        assertEquals(AccountException.DEFAULT_ERROR_CODE, error.errorCode());
        assertEquals(AccountException.DEFAULT_ERROR_MESSAGE, error.errorMessage());
        assertEquals(AccountException.DEFAULT_ERROR_CAUSE, error.errorCause());
    }
}