package com.java.admin.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.admin.constant.ApiAccountEndpoints;
import com.java.admin.controller.account.AccountController;
import com.java.admin.dto.account.request.*;
import com.java.admin.dto.account.response.*;
import com.java.admin.usecase.account.IAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private IAccountService accountService;

    @InjectMocks
    private AccountController accountController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(accountController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void createUserAccount_shouldReturnCreated() throws Exception {
        // Arrange
        CreateAccountRequestDto requestDto = new CreateAccountRequestDto("test@example.com", "Password123!", "John.doe@gmail.com", "John", "Doe", "123456789", "example street");
        CreateAccountResponseDto responseDto = new CreateAccountResponseDto("Account created successfully");
        when(accountService.createAccount(any(CreateAccountRequestDto.class))).thenReturn(responseDto);

        // Act & Assert
        mockMvc.perform(post(ApiAccountEndpoints.BASE_PATH + ApiAccountEndpoints.CREATE_ACCOUNT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.email").value("test@example.com"));

        verify(accountService, times(1)).createAccount(any(CreateAccountRequestDto.class));
    }

    @Test
    void verifyAccount_shouldReturnOk() throws Exception {
        // Arrange
        String token = "valid-token";
        VerifyAccountResponseDto responseDto = new VerifyAccountResponseDto("Account verified successfully", "token aksjdskajd");
        when(accountService.verifyAccount(any(VerifyAccountRequestDto.class))).thenReturn(responseDto);

        // Act & Assert
        mockMvc.perform(get(ApiAccountEndpoints.BASE_PATH + ApiAccountEndpoints.VERIFY_ACCOUNT, token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.message").value("Account verified successfully"));

        verify(accountService, times(1)).verifyAccount(any(VerifyAccountRequestDto.class));
    }

    @Test
    void resendVerificationCode_shouldReturnOk() throws Exception {
        // Arrange
        ResendVerificationCodeAccountRequestDto requestDto = new ResendVerificationCodeAccountRequestDto("test@example.com");
        ResendVerificationCodeAccountResponseDto responseDto = new ResendVerificationCodeAccountResponseDto("Verification code resent");
        when(accountService.resendVerificationCode(any())).thenReturn(responseDto);

        // Act & Assert
        mockMvc.perform(post(ApiAccountEndpoints.BASE_PATH + ApiAccountEndpoints.RESEND_VERIFICATION_CODE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.message").value("Verification code resent"));

        verify(accountService, times(1)).resendVerificationCode(any());
    }

    @Test
    void validateCode_shouldReturnOk() throws Exception {
        // Arrange
        ValidateCodeAccountRequestDto requestDto = new ValidateCodeAccountRequestDto("test@example.com", "123456", "valid-token", "Password123!");
        ValidateCodeAccountResponseDto responseDto = new ValidateCodeAccountResponseDto("Code validated successfully");
        when(accountService.validateCode(any())).thenReturn(responseDto);

        // Act & Assert
        mockMvc.perform(post(ApiAccountEndpoints.BASE_PATH + ApiAccountEndpoints.VALIDATE_CODE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.message").value("Code validated successfully"));

        verify(accountService, times(1)).validateCode(any());
    }

    @Test
    void resetPassword_shouldReturnOk() throws Exception {
        // Arrange
        ResetPasswordRequestDto requestDto = new ResetPasswordRequestDto("test@example.com");
        ResetPasswordResponseDto responseDto = new ResetPasswordResponseDto("Password reset email sent");
        when(accountService.resetPassword(any())).thenReturn(responseDto);

        // Act & Assert
        mockMvc.perform(post(ApiAccountEndpoints.BASE_PATH + ApiAccountEndpoints.RESET_PASSWORD)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.message").value("Password reset email sent"));

        verify(accountService, times(1)).resetPassword(any());
    }

    @Test
    void validateResetPasswordToken_shouldReturnOk() throws Exception {
        // Arrange
        String token = "valid-reset-token";
        ResetPasswordTokenResponseDto responseDto = new ResetPasswordTokenResponseDto("Token is valid", "valid-reset-token");
        when(accountService.resetPasswordToken(any())).thenReturn(responseDto);

        // Act & Assert
        mockMvc.perform(get(ApiAccountEndpoints.BASE_PATH + ApiAccountEndpoints.VALIDATE_RESET_PASSWORD, token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.message").value("Token is valid"));

        verify(accountService, times(1)).resetPasswordToken(any());
    }

    @Test
    void changePassword_shouldReturnOk() throws Exception {
        // Arrange
        ChangePasswordRequestDto requestDto = new ChangePasswordRequestDto("test@example.com", "NewPassword123!", "token4", "OldPassword123!");
        ChangePasswordResponseDto responseDto = new ChangePasswordResponseDto("Password changed successfully");
        when(accountService.changePassword(any())).thenReturn(responseDto);

        // Act & Assert
        mockMvc.perform(post(ApiAccountEndpoints.BASE_PATH + ApiAccountEndpoints.CHANGE_PASSWORD)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.message").value("Password changed successfully"));

        verify(accountService, times(1)).changePassword(any());
    }
}