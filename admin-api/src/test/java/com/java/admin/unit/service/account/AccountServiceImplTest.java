package com.java.admin.unit.service.account;

import com.java.admin.config.ServicePropertiesConfig;
import com.java.admin.dto.account.request.ChangePasswordRequestDto;
import com.java.admin.dto.account.request.CreateAccountRequestDto;
import com.java.admin.dto.account.request.ResendVerificationCodeAccountRequestDto;
import com.java.admin.dto.account.request.ResetPasswordRequestDto;
import com.java.admin.dto.account.request.ResetPasswordTokenRequestDto;
import com.java.admin.dto.account.request.ValidateCodeAccountRequestDto;
import com.java.admin.dto.account.request.VerifyAccountRequestDto;
import com.java.admin.dto.account.response.ChangePasswordResponseDto;
import com.java.admin.dto.account.response.CreateAccountResponseDto;
import com.java.admin.dto.account.response.ResendVerificationCodeAccountResponseDto;
import com.java.admin.dto.account.response.ResetPasswordResponseDto;
import com.java.admin.dto.account.response.ResetPasswordTokenResponseDto;
import com.java.admin.dto.account.response.ValidateCodeAccountResponseDto;
import com.java.admin.dto.account.response.VerifyAccountResponseDto;
import com.java.admin.entity.user.UserEntity;
import com.java.admin.exception.account.AccountException;
import com.java.admin.mapper.account.AccountMapper;
import com.java.admin.repository.user.UserRepository;
import com.java.admin.security.AuthProvider;
import com.java.admin.service.account.AccountServiceImpl;
import com.java.admin.util.SendMailUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.OffsetDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private SendMailUtil sendMailUtil;

    @Mock
    private AccountMapper accountMapper;

    @Mock
    private ServicePropertiesConfig servicePropertiesConfig;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AccountServiceImpl accountService;

    private CreateAccountRequestDto createAccountRequest;
    private UserEntity userEntity;
    private VerifyAccountRequestDto verifyAccountRequest;
    private ValidateCodeAccountRequestDto validateCodeRequest;
    private ResetPasswordRequestDto resetPasswordRequest;
    private ChangePasswordRequestDto changePasswordRequest;

    @BeforeEach
    void setUp() {
        createAccountRequest = new CreateAccountRequestDto(
                "testUser",
                "password123",
                "test@example.com",
                "John",
                "Doe",
                "+1234567890",
                "123 Main St"
        );

        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setEmail("test@example.com");
        userEntity.setFirstName("John");
        userEntity.setPassword("$2a$10$encodedPassword");
        userEntity.setVerificationToken("test-token");
        userEntity.setVerificationCode("123456");
        userEntity.setVerificationTokenExpiry(OffsetDateTime.now().plusDays(1));
        userEntity.setMaxVerificationCodeAttempts(0);
        userEntity.setMaxResetPasswordAttempts(0);
        userEntity.setEnabled(false);

        verifyAccountRequest = new VerifyAccountRequestDto("test-token");
        validateCodeRequest = new ValidateCodeAccountRequestDto("test@example.com", "123456", "password123", "validPassword123");
        resetPasswordRequest = new ResetPasswordRequestDto("test@example.com");
        changePasswordRequest = new ChangePasswordRequestDto("test@example.com", "123456", "newPassword123", "validPassword123");
    }

    @Test
    void createAccount_shouldCreateAccountSuccessfully_whenValidRequest() {
        // Arrange
        when(userRepository.existsUserEntityByEmail(createAccountRequest.email())).thenReturn(false);
        when(accountMapper.toEntity(createAccountRequest, AuthProvider.LOCAL)).thenReturn(userEntity);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        when(servicePropertiesConfig.getBaseHostPath()).thenReturn("http://localhost:8080");

        // Act
        CreateAccountResponseDto response = accountService.createAccount(createAccountRequest);

        // Assert
        assertNotNull(response);
        assertEquals("User account created successfully with email: test@example.com", response.message());
        verify(userRepository).existsUserEntityByEmail(createAccountRequest.email());
        verify(accountMapper).toEntity(createAccountRequest, AuthProvider.LOCAL);
        verify(userRepository).save(any(UserEntity.class));
    }

    @Test
    void createAccount_shouldThrowAccountException_whenUserAlreadyExists() {
        // Arrange
        when(userRepository.existsUserEntityByEmail(createAccountRequest.email())).thenReturn(true);

        // Act & Assert
        AccountException exception = assertThrows(AccountException.class,
                () -> accountService.createAccount(createAccountRequest));

        assertEquals(AccountException.USER_ACCOUNT_ALREADY_EXISTS_CODE, exception.getErrorCode());
        assertEquals(AccountException.USER_ACCOUNT_ALREADY_EXISTS_MESSAGE, exception.getErrorMessage());
        assertEquals(AccountException.USER_ACCOUNT_ALREADY_EXISTS_CAUSE, exception.getErrorCause());
        verify(userRepository, never()).save(any(UserEntity.class));
    }

    @Test
    void verifyAccount_shouldReturnSuccessResponse_whenValidToken() {
        // Arrange
        when(userRepository.findUserEntityByVerificationToken(verifyAccountRequest.verificationToken()))
                .thenReturn(Optional.of(userEntity));

        // Act
        VerifyAccountResponseDto response = accountService.verifyAccount(verifyAccountRequest);

        // Assert
        assertNotNull(response);
        assertTrue(response.message().contains("Token verified is still valid"));
        assertEquals(verifyAccountRequest.verificationToken(), response.verificationToken());
        verify(userRepository).findUserEntityByVerificationToken(verifyAccountRequest.verificationToken());
    }

    @Test
    void verifyAccount_shouldThrowAccountException_whenTokenNotFound() {
        // Arrange
        when(userRepository.findUserEntityByVerificationToken(verifyAccountRequest.verificationToken()))
                .thenReturn(Optional.empty());

        // Act & Assert
        AccountException exception = assertThrows(AccountException.class,
                () -> accountService.verifyAccount(verifyAccountRequest));

        assertEquals(AccountException.USER_ACCOUNT_NOT_FOUND_CODE, exception.getErrorCode());
        assertEquals(AccountException.USER_ACCOUNT_NOT_FOUND_MESSAGE, exception.getErrorMessage());
        assertEquals(AccountException.USER_ACCOUNT_NOT_FOUND_CAUSE, exception.getErrorCause());
    }

    @Test
    void verifyAccount_shouldThrowAccountException_whenTokenExpired() {
        // Arrange
        userEntity.setVerificationTokenExpiry(OffsetDateTime.now().minusDays(1));
        when(userRepository.findUserEntityByVerificationToken(verifyAccountRequest.verificationToken()))
                .thenReturn(Optional.of(userEntity));

        // Act & Assert
        AccountException exception = assertThrows(AccountException.class,
                () -> accountService.verifyAccount(verifyAccountRequest));

        assertEquals(AccountException.VERIFICATION_TOKEN_EXPIRED_CODE, exception.getErrorCode());
        assertEquals(AccountException.VERIFICATION_TOKEN_EXPIRED_MESSAGE, exception.getErrorMessage());
        assertEquals(AccountException.VERIFICATION_TOKEN_EXPIRED_CAUSE, exception.getErrorCause());
    }

    @Test
    void validateCode_shouldValidateSuccessfully_whenValidCodeAndPassword() {
        // Arrange
        when(userRepository.findUserEntityByEmail(validateCodeRequest.email()))
                .thenReturn(Optional.of(userEntity));
        when(servicePropertiesConfig.getMaxVerificationCodeAttempts()).thenReturn(3);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        when(passwordEncoder.matches(validateCodeRequest.password(), userEntity.getPassword()))
                .thenReturn(true);

        // Asegurar que el código de verificación coincida
        userEntity.setVerificationCode(validateCodeRequest.verificationCode());

        // Act
        ValidateCodeAccountResponseDto response = accountService.validateCode(validateCodeRequest);

        // Assert
        assertNotNull(response);
        assertTrue(response.message().contains("has been successfully validated and enabled"));
        verify(userRepository).save(any(UserEntity.class));
        verify(passwordEncoder).matches(validateCodeRequest.password(), userEntity.getPassword());
    }

    @Test
    void validateCode_shouldThrowAccountException_whenUserNotFound() {
        // Arrange
        when(userRepository.findUserEntityByEmail(validateCodeRequest.email()))
                .thenReturn(Optional.empty());

        // Act & Assert
        AccountException exception = assertThrows(AccountException.class,
                () -> accountService.validateCode(validateCodeRequest));

        assertEquals(AccountException.USER_ACCOUNT_NOT_FOUND_CODE, exception.getErrorCode());
        assertEquals(AccountException.USER_ACCOUNT_NOT_FOUND_MESSAGE, exception.getErrorMessage());
        assertEquals(AccountException.USER_ACCOUNT_NOT_FOUND_CAUSE, exception.getErrorCause());
    }

    @Test
    void validateCode_shouldThrowAccountException_whenTokenExpired() {
        // Arrange
        userEntity.setVerificationTokenExpiry(OffsetDateTime.now().minusDays(1));
        when(userRepository.findUserEntityByEmail(validateCodeRequest.email()))
                .thenReturn(Optional.of(userEntity));

        // Act & Assert
        AccountException exception = assertThrows(AccountException.class,
                () -> accountService.validateCode(validateCodeRequest));

        assertEquals(AccountException.VERIFICATION_TOKEN_EXPIRED_CODE, exception.getErrorCode());
        assertEquals(AccountException.VERIFICATION_TOKEN_EXPIRED_MESSAGE, exception.getErrorMessage());
        assertEquals(AccountException.VERIFICATION_TOKEN_EXPIRED_CAUSE, exception.getErrorCause());
    }

    @Test
    void validateCode_shouldThrowAccountException_whenMaxAttemptsReached() {
        // Arrange
        userEntity.setMaxVerificationCodeAttempts(3);
        when(userRepository.findUserEntityByEmail(validateCodeRequest.email()))
                .thenReturn(Optional.of(userEntity));
        when(servicePropertiesConfig.getMaxVerificationCodeAttempts()).thenReturn(3);

        // Act & Assert
        AccountException exception = assertThrows(AccountException.class,
                () -> accountService.validateCode(validateCodeRequest));

        assertEquals(AccountException.MAX_VERIFICATION_CODE_ATTEMPTS_REACHED_CODE, exception.getErrorCode());
        assertEquals(AccountException.MAX_VERIFICATION_CODE_ATTEMPTS_REACHED_MESSAGE, exception.getErrorMessage());
        assertEquals(AccountException.MAX_VERIFICATION_CODE_ATTEMPTS_REACHED_CAUSE, exception.getErrorCause());
    }

    @Test
    void validateCode_shouldThrowAccountException_whenInvalidCodeOrPassword() {
        // Arrange
        userEntity.setVerificationCode("wrong-code");
        when(userRepository.findUserEntityByEmail(validateCodeRequest.email()))
                .thenReturn(Optional.of(userEntity));
        when(servicePropertiesConfig.getMaxVerificationCodeAttempts()).thenReturn(3);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        when(passwordEncoder.matches(validateCodeRequest.password(), userEntity.getPassword()))
                .thenReturn(false);

        // Act & Assert
        AccountException exception = assertThrows(AccountException.class,
                () -> accountService.validateCode(validateCodeRequest));

        assertEquals(AccountException.INVALID_VERIFICATION_CODE_OR_PASSWORD_CODE, exception.getErrorCode());
        assertEquals(AccountException.INVALID_VERIFICATION_CODE_OR_PASSWORD_MESSAGE, exception.getErrorMessage());
        assertEquals(AccountException.INVALID_VERIFICATION_CODE_OR_PASSWORD_CAUSE, exception.getErrorCause());
        verify(passwordEncoder).matches(validateCodeRequest.password(), userEntity.getPassword());
    }

    @Test
    void resetPassword_shouldSendResetCodeSuccessfully_whenValidUser() {
        // Arrange
        userEntity.setEnabled(true);
        when(userRepository.findUserEntityByEmail(resetPasswordRequest.email()))
                .thenReturn(Optional.of(userEntity));
        when(servicePropertiesConfig.getMaxResetPasswordAttempts()).thenReturn(3);
        when(servicePropertiesConfig.getBaseHostPath()).thenReturn("http://localhost:8080");
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        // Act
        ResetPasswordResponseDto response = accountService.resetPassword(resetPasswordRequest);

        // Assert
        assertNotNull(response);
        assertTrue(response.message().contains("Reset password verificationCode sent to email"));
        verify(userRepository).save(any(UserEntity.class));
    }

    @Test
    void resetPassword_shouldThrowAccountException_whenUserNotFound() {
        // Arrange
        when(userRepository.findUserEntityByEmail(resetPasswordRequest.email()))
                .thenReturn(Optional.empty());

        // Act & Assert
        AccountException exception = assertThrows(AccountException.class,
                () -> accountService.resetPassword(resetPasswordRequest));

        assertEquals(AccountException.USER_ACCOUNT_NOT_FOUND_CODE, exception.getErrorCode());
        assertEquals(AccountException.USER_ACCOUNT_NOT_FOUND_MESSAGE, exception.getErrorMessage());
        assertEquals(AccountException.USER_ACCOUNT_NOT_FOUND_CAUSE, exception.getErrorCause());
    }

    @Test
    void resetPassword_shouldThrowAccountException_whenUserNotEnabled() {
        // Arrange
        userEntity.setEnabled(false);
        when(userRepository.findUserEntityByEmail(resetPasswordRequest.email()))
                .thenReturn(Optional.of(userEntity));

        // Act & Assert
        AccountException exception = assertThrows(AccountException.class,
                () -> accountService.resetPassword(resetPasswordRequest));

        assertEquals(AccountException.USER_ACCOUNT_NOT_ENABLED_CODE, exception.getErrorCode());
        assertEquals(AccountException.USER_ACCOUNT_NOT_ENABLED_MESSAGE, exception.getErrorMessage());
        assertEquals(AccountException.USER_ACCOUNT_NOT_ENABLED_CAUSE, exception.getErrorCause());
    }

    @Test
    void resetPassword_shouldThrowAccountException_whenMaxAttemptsReached() {
        // Arrange
        userEntity.setEnabled(true);
        userEntity.setMaxResetPasswordAttempts(3);
        when(userRepository.findUserEntityByEmail(resetPasswordRequest.email()))
                .thenReturn(Optional.of(userEntity));
        when(servicePropertiesConfig.getMaxResetPasswordAttempts()).thenReturn(3);

        // Act & Assert
        AccountException exception = assertThrows(AccountException.class,
                () -> accountService.resetPassword(resetPasswordRequest));

        assertEquals(AccountException.MAX_RESET_PASSWORD_ATTEMPTS_REACHED_CODE, exception.getErrorCode());
        assertEquals(AccountException.MAX_RESET_PASSWORD_ATTEMPTS_REACHED_MESSAGE, exception.getErrorMessage());
        assertEquals(AccountException.MAX_RESET_PASSWORD_ATTEMPTS_REACHED_CAUSE, exception.getErrorCause());
    }

    @Test
    void changePassword_shouldChangePasswordSuccessfully_whenValidCode() {
        // Arrange
        userEntity.setResetPasswordCode(changePasswordRequest.resetPasswordCode()); // Usar el mismo código del request
        userEntity.setResetPasswordTokenExpiry(OffsetDateTime.now().plusHours(1));
        when(userRepository.findUserEntityByEmail(changePasswordRequest.email()))
                .thenReturn(Optional.of(userEntity));
        when(servicePropertiesConfig.getMaxResetPasswordAttempts()).thenReturn(3);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        when(passwordEncoder.encode(changePasswordRequest.newPassword()))
                .thenReturn("$2a$10$newEncodedPassword");

        // Act
        ChangePasswordResponseDto response = accountService.changePassword(changePasswordRequest);

        // Assert
        assertNotNull(response);
        assertTrue(response.message().contains("Password changed successfully"));
        verify(userRepository).save(any(UserEntity.class));
        verify(passwordEncoder).encode(changePasswordRequest.newPassword());
    }

    @Test
    void changePassword_shouldThrowAccountException_whenUserNotFound() {
        // Arrange
        when(userRepository.findUserEntityByEmail(changePasswordRequest.email()))
                .thenReturn(Optional.empty());

        // Act & Assert
        AccountException exception = assertThrows(AccountException.class,
                () -> accountService.changePassword(changePasswordRequest));

        assertEquals(AccountException.USER_ACCOUNT_NOT_FOUND_CODE, exception.getErrorCode());
        assertEquals(AccountException.USER_ACCOUNT_NOT_FOUND_MESSAGE, exception.getErrorMessage());
        assertEquals(AccountException.USER_ACCOUNT_NOT_FOUND_CAUSE, exception.getErrorCause());
    }

    @Test
    void changePassword_shouldThrowAccountException_whenTokenExpired() {
        // Arrange
        userEntity.setResetPasswordTokenExpiry(OffsetDateTime.now().minusHours(1));
        when(userRepository.findUserEntityByEmail(changePasswordRequest.email()))
                .thenReturn(Optional.of(userEntity));

        // Act & Assert
        AccountException exception = assertThrows(AccountException.class,
                () -> accountService.changePassword(changePasswordRequest));

        assertEquals(AccountException.RESET_PASSWORD_TOKEN_EXPIRED_CODE, exception.getErrorCode());
        assertEquals(AccountException.RESET_PASSWORD_TOKEN_EXPIRED_MESSAGE, exception.getErrorMessage());
        assertEquals(AccountException.RESET_PASSWORD_TOKEN_EXPIRED_CAUSE, exception.getErrorCause());
    }

    @Test
    void changePassword_shouldThrowAccountException_whenInvalidResetCode() {
        // Arrange
        userEntity.setResetPasswordCode("wrong-code");
        userEntity.setResetPasswordTokenExpiry(OffsetDateTime.now().plusHours(1));
        when(userRepository.findUserEntityByEmail(changePasswordRequest.email()))
                .thenReturn(Optional.of(userEntity));
        when(servicePropertiesConfig.getMaxResetPasswordAttempts()).thenReturn(3);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        // Act & Assert
        AccountException exception = assertThrows(AccountException.class,
                () -> accountService.changePassword(changePasswordRequest));

        assertEquals(AccountException.INVALID_RESET_PASSWORD_CODE_OR_CURRENT_PASSWORD_CODE, exception.getErrorCode());
        assertEquals(AccountException.INVALID_RESET_PASSWORD_CODE_OR_CURRENT_PASSWORD_MESSAGE, exception.getErrorMessage());
        assertEquals(AccountException.INVALID_RESET_PASSWORD_CODE_OR_CURRENT_PASSWORD_CAUSE, exception.getErrorCause());
    }

    @Test
    void changePassword_shouldThrowAccountException_whenMaxResetPasswordAttemptsReached() {
        // Arrange
        userEntity.setResetPasswordCode(changePasswordRequest.resetPasswordCode());
        userEntity.setResetPasswordTokenExpiry(OffsetDateTime.now().plusHours(1));
        userEntity.setMaxResetPasswordAttempts(3); // Configurar que ya llegó al máximo
        when(userRepository.findUserEntityByEmail(changePasswordRequest.email()))
                .thenReturn(Optional.of(userEntity));
        when(servicePropertiesConfig.getMaxResetPasswordAttempts()).thenReturn(3);

        // Act & Assert
        AccountException exception = assertThrows(AccountException.class,
                () -> accountService.changePassword(changePasswordRequest));

        assertEquals(AccountException.MAX_RESET_PASSWORD_ATTEMPTS_REACHED_CODE, exception.getErrorCode());
        assertEquals(AccountException.MAX_RESET_PASSWORD_ATTEMPTS_REACHED_MESSAGE, exception.getErrorMessage());
        assertEquals(AccountException.MAX_RESET_PASSWORD_ATTEMPTS_REACHED_CAUSE, exception.getErrorCause());
        verify(userRepository).findUserEntityByEmail(changePasswordRequest.email());
        verify(userRepository, never()).save(any(UserEntity.class));
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    void resendVerificationCode_shouldResendCodeSuccessfully_whenValidUser() {
        // Arrange
        ResendVerificationCodeAccountRequestDto request = new ResendVerificationCodeAccountRequestDto("test@example.com");
        when(userRepository.findUserEntityByEmail(request.email()))
                .thenReturn(Optional.of(userEntity));
        when(servicePropertiesConfig.getBaseHostPath()).thenReturn("http://localhost:8080");
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        // Act
        ResendVerificationCodeAccountResponseDto response = accountService.resendVerificationCode(request);

        // Assert
        assertNotNull(response);
        assertTrue(response.message().contains("Verification verificationCode resent successfully"));
        verify(userRepository).save(any(UserEntity.class));
    }

    @Test
    void resendVerificationCode_shouldThrowAccountException_whenUserAlreadyEnabled() {
        // Arrange
        userEntity.setEnabled(true);
        ResendVerificationCodeAccountRequestDto request = new ResendVerificationCodeAccountRequestDto("test@example.com");
        when(userRepository.findUserEntityByEmail(request.email()))
                .thenReturn(Optional.of(userEntity));

        // Act & Assert
        AccountException exception = assertThrows(AccountException.class,
                () -> accountService.resendVerificationCode(request));

        assertEquals(AccountException.USER_ACCOUNT_ALREADY_ENABLED_CODE, exception.getErrorCode());
        assertEquals(AccountException.USER_ACCOUNT_ALREADY_ENABLED_MESSAGE, exception.getErrorMessage());
        assertEquals(AccountException.USER_ACCOUNT_ALREADY_ENABLED_CAUSE, exception.getErrorCause());
    }

    @Test
    void resendVerificationCode_shouldThrowAccountException_whenUserNotFound() {
        // Arrange
        ResendVerificationCodeAccountRequestDto request = new ResendVerificationCodeAccountRequestDto("nonexistent@example.com");
        when(userRepository.findUserEntityByEmail(request.email()))
                .thenReturn(Optional.empty());

        // Act & Assert
        AccountException exception = assertThrows(AccountException.class,
                () -> accountService.resendVerificationCode(request));

        assertEquals(AccountException.USER_ACCOUNT_NOT_FOUND_CODE, exception.getErrorCode());
        assertEquals(AccountException.USER_ACCOUNT_NOT_FOUND_MESSAGE, exception.getErrorMessage());
        assertEquals(AccountException.USER_ACCOUNT_NOT_FOUND_CAUSE, exception.getErrorCause());
        verify(userRepository).findUserEntityByEmail(request.email());
        verify(userRepository, never()).save(any(UserEntity.class));
    }

    @Test
    void resetPasswordToken_shouldReturnValidResponse_whenTokenValid() {
        // Arrange
        userEntity.setResetPasswordTokenExpiry(OffsetDateTime.now().plusHours(1));
        ResetPasswordTokenRequestDto request = new ResetPasswordTokenRequestDto("reset-token");
        when(userRepository.findUserEntityByResetPasswordToken(request.resetPasswordToken()))
                .thenReturn(Optional.of(userEntity));

        // Act
        ResetPasswordTokenResponseDto response = accountService.resetPasswordToken(request);

        // Assert
        assertNotNull(response);
        assertTrue(response.message().contains("Reset password token is still valid"));
        assertEquals(request.resetPasswordToken(), response.resetPasswordToken());
    }

    @Test
    void resetPasswordToken_shouldThrowAccountException_whenTokenNotFound() {
        // Arrange
        ResetPasswordTokenRequestDto request = new ResetPasswordTokenRequestDto("invalid-token");
        when(userRepository.findUserEntityByResetPasswordToken(request.resetPasswordToken()))
                .thenReturn(Optional.empty());

        // Act & Assert
        AccountException exception = assertThrows(AccountException.class,
                () -> accountService.resetPasswordToken(request));

        assertEquals(AccountException.USER_ACCOUNT_NOT_FOUND_CODE, exception.getErrorCode());
        assertEquals(AccountException.USER_ACCOUNT_NOT_FOUND_MESSAGE, exception.getErrorMessage());
        assertEquals(AccountException.USER_ACCOUNT_NOT_FOUND_CAUSE, exception.getErrorCause());
    }

    @Test
    void resetPasswordToken_shouldThrowAccountException_whenTokenExpired() {
        // Arrange
        userEntity.setResetPasswordTokenExpiry(OffsetDateTime.now().minusHours(1));
        ResetPasswordTokenRequestDto request = new ResetPasswordTokenRequestDto("expired-token");
        when(userRepository.findUserEntityByResetPasswordToken(request.resetPasswordToken()))
                .thenReturn(Optional.of(userEntity));

        // Act & Assert
        AccountException exception = assertThrows(AccountException.class,
                () -> accountService.resetPasswordToken(request));

        assertEquals(AccountException.RESET_PASSWORD_TOKEN_EXPIRED_CODE, exception.getErrorCode());
        assertEquals(AccountException.RESET_PASSWORD_TOKEN_EXPIRED_MESSAGE, exception.getErrorMessage());
        assertEquals(AccountException.RESET_PASSWORD_TOKEN_EXPIRED_CAUSE, exception.getErrorCause());
    }
}