package com.java.admin.unit.controller.account;

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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {

    private static final String VALID_USERNAME = "johndoe232";
    private static final String VALID_PASSWORD = "Password123!";
    private static final String VALID_EMAIL = "john.doe@example.com";
    private static final String VALID_FIRST_NAME = "John";
    private static final String VALID_LAST_NAME = "Doe";
    private static final String VALID_PHONE_NUMBER = "01234567890";
    private static final String VALID_ADDRESS = "123 Example Street";
    private static final String VALID_TOKEN = "1j2k3l4m5n6o7p8q9r0s1t2u33xr";
    private static final String VALID_VERIFICATION_CODE = "A3NKL2";

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
        CreateAccountRequestDto requestDto = new CreateAccountRequestDto(
                VALID_USERNAME,
                VALID_PASSWORD,
                VALID_EMAIL,
                VALID_FIRST_NAME,
                VALID_LAST_NAME,
                VALID_PHONE_NUMBER,
                VALID_ADDRESS);
        CreateAccountResponseDto responseDto = new CreateAccountResponseDto("Account created successfully");
        when(accountService.createAccount(any(CreateAccountRequestDto.class))).thenReturn(responseDto);

        // Act
        ResultActions resultActions = mockMvc.perform(post(ApiAccountEndpoints.BASE_PATH + ApiAccountEndpoints.CREATE_ACCOUNT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));

        // Assert
        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.message").value("Account created successfully"));

        // Verify that the service method was called
        verify(accountService, times(1)).createAccount(any(CreateAccountRequestDto.class));
    }

    @Test
    void verifyAccount_shouldReturnOk() throws Exception {
        // Arrange
        VerifyAccountResponseDto responseDto = new VerifyAccountResponseDto("Account verified successfully", VALID_TOKEN);
        when(accountService.verifyAccount(any(VerifyAccountRequestDto.class))).thenReturn(responseDto);

        // Act & Assert
        mockMvc.perform(get(ApiAccountEndpoints.BASE_PATH + ApiAccountEndpoints.VERIFY_ACCOUNT, VALID_TOKEN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.message").value("Account verified successfully"));

        verify(accountService, times(1)).verifyAccount(any(VerifyAccountRequestDto.class));
    }

    @Test
    void resendVerificationCode_shouldReturnOk() throws Exception {
        // Arrange
        ResendVerificationCodeAccountRequestDto requestDto = new ResendVerificationCodeAccountRequestDto(VALID_EMAIL);
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
        ValidateCodeAccountRequestDto requestDto = new ValidateCodeAccountRequestDto(
                VALID_EMAIL, VALID_TOKEN, VALID_VERIFICATION_CODE, VALID_PASSWORD);
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