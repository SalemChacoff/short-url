package com.java.admin.unit.exception;

import com.java.admin.constant.ApiAccountEndpoints;
import com.java.admin.constant.ApiAuthEndpoints;
import com.java.admin.constant.ApiUrlEndpoints;
import com.java.admin.constant.ApiUserEndpoints;
import com.java.admin.constant.ConstraintsApi;
import com.java.admin.dto.ApiError;
import com.java.admin.dto.ApiResponseDto;
import com.java.admin.exception.ArgumentExceptionHandler;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArgumentExceptionHandlerTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private MethodArgumentNotValidException methodArgumentNotValidException;

    @InjectMocks
    private ArgumentExceptionHandler argumentExceptionHandler;

    @BeforeEach
    void setUp() {
        // Setup común si es necesario
    }

    @Test
    void handleValidationExceptions_shouldReturnBadRequest_whenSingleFieldError() {

        // Arrange
        FieldError fieldError = new FieldError("object", "username", "Username is required");
        when(methodArgumentNotValidException.getFieldErrors()).thenReturn(List.of(fieldError));
        when(request.getRequestURI()).thenReturn(ApiAccountEndpoints.BASE_PATH + ApiAccountEndpoints.CREATE_ACCOUNT);

        // Act
        ResponseEntity<ApiResponseDto> response = argumentExceptionHandler
                .handleValidationExceptions(methodArgumentNotValidException, request);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().success());
        assertEquals(1, response.getBody().errors().size());

        ApiError error = response.getBody().errors().getFirst();
        assertEquals("Username is required", error.errorCause());
        assertEquals("username", error.errorMessage());
        assertEquals(ApiAccountEndpoints.getAccountCodeFieldsValidation("username"), error.errorCode());
    }

    @Test
    void handleValidationExceptions_shouldReturnBadRequest_whenMultipleFieldErrors() {
        // Arrange
        FieldError usernameError = new FieldError("object", "username", "Username is required");
        FieldError emailError = new FieldError("object", "email", "Email should be valid");
        when(methodArgumentNotValidException.getFieldErrors()).thenReturn(List.of(usernameError, emailError));
        when(request.getRequestURI()).thenReturn(ApiAccountEndpoints.BASE_PATH + ApiAccountEndpoints.CREATE_ACCOUNT);

        // Act
        ResponseEntity<ApiResponseDto> response = argumentExceptionHandler
                .handleValidationExceptions(methodArgumentNotValidException, request);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().success());
        assertEquals(2, response.getBody().errors().size());
    }

    @Test
    void handleValidationExceptions_shouldReturnAuthErrorCode_whenAuthPath() {
        // Arrange
        FieldError fieldError = new FieldError("object", "email", "Email is required");
        when(methodArgumentNotValidException.getFieldErrors()).thenReturn(List.of(fieldError));
        when(request.getRequestURI()).thenReturn(ApiAuthEndpoints.BASE_PATH + "/login");

        // Act
        ResponseEntity<ApiResponseDto> response = argumentExceptionHandler
                .handleValidationExceptions(methodArgumentNotValidException, request);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().success());
        assertEquals(1, response.getBody().errors().size());

        ApiError error = response.getBody().errors().getFirst();
        assertEquals("Email is required", error.errorCause());
        assertEquals("email", error.errorMessage());
        assertEquals(ApiAuthEndpoints.getAuthCodeFieldsValidation("email"), error.errorCode());
    }

    @Test
    void handleValidationExceptions_shouldReturnUrlErrorCode_whenUrlPath() {
        // Arrange
        FieldError fieldError = new FieldError("object", "url", "URL is required");
        when(methodArgumentNotValidException.getFieldErrors()).thenReturn(List.of(fieldError));
        when(request.getRequestURI()).thenReturn(ApiUrlEndpoints.BASE_PATH + "/shorten");

        // Act
        ResponseEntity<ApiResponseDto> response = argumentExceptionHandler
                .handleValidationExceptions(methodArgumentNotValidException, request);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().success());
        assertEquals(1, response.getBody().errors().size());

        ApiError error = response.getBody().errors().getFirst();
        assertEquals("URL is required", error.errorCause());
        assertEquals("url", error.errorMessage());
        assertEquals(ApiUrlEndpoints.getUrlCodeFieldsValidation("url"), error.errorCode());
    }

    @Test
    void handleValidationExceptions_shouldReturnUserErrorCode_whenUserPath() {
        // Arrange
        FieldError fieldError = new FieldError("object", "username", "Username is required");
        when(methodArgumentNotValidException.getFieldErrors()).thenReturn(List.of(fieldError));
        when(request.getRequestURI()).thenReturn(ApiUserEndpoints.BASE_PATH + "/profile");

        // Act
        ResponseEntity<ApiResponseDto> response = argumentExceptionHandler
                .handleValidationExceptions(methodArgumentNotValidException, request);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().success());
        assertEquals(1, response.getBody().errors().size());

        ApiError error = response.getBody().errors().getFirst();
        assertEquals("Username is required", error.errorCause());
        assertEquals("username", error.errorMessage());
        assertEquals(ApiUserEndpoints.getUserCodeFieldsValidation("username"), error.errorCode());
    }

    @Test
    void handleIllegalArgumentException_shouldReturnBadRequest() {
        // Arrange
        String errorMessage = "Invalid argument provided";
        IllegalArgumentException exception = new IllegalArgumentException(errorMessage);

        // Act
        ResponseEntity<ApiResponseDto> response = argumentExceptionHandler
                .handleIllegalArgumentException(exception);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().success());
        assertEquals(1, response.getBody().errors().size());

        ApiError error = response.getBody().errors().getFirst();
        assertEquals(errorMessage, error.errorCause());
        assertEquals(ConstraintsApi.ILLEGAL_ARGUMENT_MESSAGE, error.errorMessage());
        assertEquals(ConstraintsApi.ILLEGAL_ARGUMENT_CODE, error.errorCode());
    }

    @Test
    void handleException_shouldReturnInternalServerError() {
        // Arrange
        String errorMessage = "Unexpected error occurred";
        Exception exception = new Exception(errorMessage);

        // Act
        ResponseEntity<ApiResponseDto> response = argumentExceptionHandler
                .handleException(exception);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().success());
        assertEquals(1, response.getBody().errors().size());

        ApiError error = response.getBody().errors().getFirst();
        assertEquals(errorMessage, error.errorCause());
        assertEquals(ConstraintsApi.GENERIC_ERROR_MESSAGE, error.errorMessage());
        assertEquals(ConstraintsApi.GENERIC_ERROR_CODE, error.errorCode());
    }


    @Test
    void handleValidationExceptions_shouldReturnDefaultErrorCode_whenUnknownPath() {
        // Arrange
        FieldError fieldError = new FieldError("object", "field", "Field error");
        when(methodArgumentNotValidException.getFieldErrors()).thenReturn(List.of(fieldError));
        when(request.getRequestURI()).thenReturn("/unknown/path");

        // Act
        ResponseEntity<ApiResponseDto> response = argumentExceptionHandler
                .handleValidationExceptions(methodArgumentNotValidException, request);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());

        ApiError error = response.getBody().errors().getFirst();
        assertEquals(0, error.errorCode()); // Default error code
    }

    @Test
    void handleIllegalArgumentException_shouldHandleNullMessage() {
        // Arrange
        IllegalArgumentException exception = new IllegalArgumentException();

        // Act
        ResponseEntity<ApiResponseDto> response = argumentExceptionHandler
                .handleIllegalArgumentException(exception);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().success());
    }
}