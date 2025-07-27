package com.java.admin.unit.exception.user;

import com.java.admin.dto.ApiError;
import com.java.admin.dto.ApiResponseDto;
import com.java.admin.exception.user.ServicesUserExceptionHandler;
import com.java.admin.exception.user.UserException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ServicesUserExceptionHandlerTest {

    @InjectMocks
    private ServicesUserExceptionHandler servicesUserExceptionHandler;

    @Test
    void handleValidationException_shouldReturnNotFound_whenUserAccountNotFound() {
        // Arrange
        UserException exception = new UserException(
                UserException.USER_ACCOUNT_NOT_FOUND_CODE,
                UserException.USER_ACCOUNT_NOT_FOUND_MESSAGE,
                UserException.USER_ACCOUNT_NOT_FOUND_CAUSE
        );

        // Act
        ResponseEntity<ApiResponseDto> response = servicesUserExceptionHandler.handleValidationException(exception);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().success());
        assertNull(response.getBody().data());
        assertEquals(1, response.getBody().errors().size());

        ApiError error = response.getBody().errors().getFirst();
        assertEquals(UserException.USER_ACCOUNT_NOT_FOUND_CODE, error.errorCode());
        assertEquals(UserException.USER_ACCOUNT_NOT_FOUND_MESSAGE, error.errorMessage());
        assertEquals(UserException.USER_ACCOUNT_NOT_FOUND_CAUSE, error.errorCause());
    }

    @Test
    void handleValidationException_shouldReturnConflict_whenUserAccountAlreadyExists() {
        // Arrange
        UserException exception = new UserException(
                UserException.USER_ACCOUNT_ALREADY_EXISTS_CODE,
                UserException.USER_ACCOUNT_ALREADY_EXISTS_MESSAGE,
                UserException.USER_ACCOUNT_ALREADY_EXISTS_CAUSE
        );

        // Act
        ResponseEntity<ApiResponseDto> response = servicesUserExceptionHandler.handleValidationException(exception);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().success());
        assertNull(response.getBody().data());
        assertEquals(1, response.getBody().errors().size());

        ApiError error = response.getBody().errors().getFirst();
        assertEquals(UserException.USER_ACCOUNT_ALREADY_EXISTS_CODE, error.errorCode());
        assertEquals(UserException.USER_ACCOUNT_ALREADY_EXISTS_MESSAGE, error.errorMessage());
        assertEquals(UserException.USER_ACCOUNT_ALREADY_EXISTS_CAUSE, error.errorCause());
    }

    @Test
    void handleValidationException_shouldReturnBadRequest_whenInvalidUserAccountData() {
        // Arrange
        UserException exception = new UserException(
                UserException.INVALID_USER_ACCOUNT_DATA_CODE,
                UserException.INVALID_USER_ACCOUNT_DATA_MESSAGE,
                UserException.INVALID_USER_ACCOUNT_DATA_CAUSE
        );

        // Act
        ResponseEntity<ApiResponseDto> response = servicesUserExceptionHandler.handleValidationException(exception);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().success());
        assertNull(response.getBody().data());
        assertEquals(1, response.getBody().errors().size());

        ApiError error = response.getBody().errors().getFirst();
        assertEquals(UserException.INVALID_USER_ACCOUNT_DATA_CODE, error.errorCode());
        assertEquals(UserException.INVALID_USER_ACCOUNT_DATA_MESSAGE, error.errorMessage());
        assertEquals(UserException.INVALID_USER_ACCOUNT_DATA_CAUSE, error.errorCause());
    }

    @Test
    void handleValidationException_shouldReturnInternalServerError_whenDefaultError() {
        // Arrange
        UserException exception = new UserException(
                UserException.DEFAULT_ERROR_CODE,
                UserException.DEFAULT_ERROR_MESSAGE,
                UserException.DEFAULT_ERROR_CAUSE
        );

        // Act
        ResponseEntity<ApiResponseDto> response = servicesUserExceptionHandler.handleValidationException(exception);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().success());
        assertNull(response.getBody().data());
        assertEquals(1, response.getBody().errors().size());

        ApiError error = response.getBody().errors().getFirst();
        assertEquals(UserException.DEFAULT_ERROR_CODE, error.errorCode());
        assertEquals(UserException.DEFAULT_ERROR_MESSAGE, error.errorMessage());
        assertEquals(UserException.DEFAULT_ERROR_CAUSE, error.errorCause());
    }
}