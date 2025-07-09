package com.java.admin.unit.exception.url;

import com.java.admin.dto.ApiError;
import com.java.admin.dto.ApiResponseDto;
import com.java.admin.exception.url.ServicesUrlExceptionHandler;
import com.java.admin.exception.url.UrlException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ServicesUrlExceptionHandlerTest {

    @InjectMocks
    private ServicesUrlExceptionHandler servicesUrlExceptionHandler;

    @Test
    void handleValidationException_shouldReturnNotFound_whenUrlNotFound() {
        // Arrange
        UrlException exception = new UrlException(
                UrlException.URL_NOT_FOUND_CODE,
                UrlException.URL_NOT_FOUND_MESSAGE,
                UrlException.URL_NOT_FOUND_CAUSE
        );

        // Act
        ResponseEntity<ApiResponseDto> response = servicesUrlExceptionHandler.handleValidationException(exception);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().success());
        assertNull(response.getBody().data());
        assertEquals(1, response.getBody().errors().size());

        ApiError error = response.getBody().errors().getFirst();
        assertEquals(UrlException.URL_NOT_FOUND_CODE, error.errorCode());
        assertEquals(UrlException.URL_NOT_FOUND_MESSAGE, error.errorMessage());
        assertEquals(UrlException.URL_NOT_FOUND_CAUSE, error.errorCause());
    }

    @Test
    void handleValidationException_shouldReturnConflict_whenUrlAlreadyExists() {
        // Arrange
        UrlException exception = new UrlException(
                UrlException.URL_ALREADY_EXISTS_CODE,
                UrlException.URL_ALREADY_EXISTS_MESSAGE,
                UrlException.URL_ALREADY_EXISTS_CAUSE
        );

        // Act
        ResponseEntity<ApiResponseDto> response = servicesUrlExceptionHandler.handleValidationException(exception);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().success());
        assertNull(response.getBody().data());
        assertEquals(1, response.getBody().errors().size());

        ApiError error = response.getBody().errors().getFirst();
        assertEquals(UrlException.URL_ALREADY_EXISTS_CODE, error.errorCode());
        assertEquals(UrlException.URL_ALREADY_EXISTS_MESSAGE, error.errorMessage());
        assertEquals(UrlException.URL_ALREADY_EXISTS_CAUSE, error.errorCause());
    }

    @Test
    void handleValidationException_shouldReturnUnprocessableEntity_whenUrlUpdateFailed() {
        // Arrange
        UrlException exception = new UrlException(
                UrlException.URL_UPDATE_FAILED_CODE,
                UrlException.URL_UPDATE_FAILED_MESSAGE,
                UrlException.URL_UPDATE_FAILED_CAUSE
        );

        // Act
        ResponseEntity<ApiResponseDto> response = servicesUrlExceptionHandler.handleValidationException(exception);

        // Assert
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().success());
        assertNull(response.getBody().data());
        assertEquals(1, response.getBody().errors().size());

        ApiError error = response.getBody().errors().getFirst();
        assertEquals(UrlException.URL_UPDATE_FAILED_CODE, error.errorCode());
        assertEquals(UrlException.URL_UPDATE_FAILED_MESSAGE, error.errorMessage());
        assertEquals(UrlException.URL_UPDATE_FAILED_CAUSE, error.errorCause());
    }

    @Test
    void handleValidationException_shouldReturnUnprocessableEntity_whenUrlDeleteFailed() {
        // Arrange
        UrlException exception = new UrlException(
                UrlException.URL_DELETE_FAILED_CODE,
                UrlException.URL_DELETE_FAILED_MESSAGE,
                UrlException.URL_DELETE_FAILED_CAUSE
        );

        // Act
        ResponseEntity<ApiResponseDto> response = servicesUrlExceptionHandler.handleValidationException(exception);

        // Assert
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().success());
        assertNull(response.getBody().data());
        assertEquals(1, response.getBody().errors().size());

        ApiError error = response.getBody().errors().getFirst();
        assertEquals(UrlException.URL_DELETE_FAILED_CODE, error.errorCode());
        assertEquals(UrlException.URL_DELETE_FAILED_MESSAGE, error.errorMessage());
        assertEquals(UrlException.URL_DELETE_FAILED_CAUSE, error.errorCause());
    }

    @Test
    void handleValidationException_shouldReturnUnprocessableEntity_whenUrlStatusUpdateFailed() {
        // Arrange
        UrlException exception = new UrlException(
                UrlException.URL_STATUS_UPDATE_FAILED_CODE,
                UrlException.URL_STATUS_UPDATE_FAILED_MESSAGE,
                UrlException.URL_STATUS_UPDATE_FAILED_CAUSE
        );

        // Act
        ResponseEntity<ApiResponseDto> response = servicesUrlExceptionHandler.handleValidationException(exception);

        // Assert
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().success());
        assertNull(response.getBody().data());
        assertEquals(1, response.getBody().errors().size());

        ApiError error = response.getBody().errors().getFirst();
        assertEquals(UrlException.URL_STATUS_UPDATE_FAILED_CODE, error.errorCode());
        assertEquals(UrlException.URL_STATUS_UPDATE_FAILED_MESSAGE, error.errorMessage());
        assertEquals(UrlException.URL_STATUS_UPDATE_FAILED_CAUSE, error.errorCause());
    }
}