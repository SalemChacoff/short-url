package com.java.admin.unit.service.auth;

import com.java.admin.dto.auth.request.LoginRequestDto;
import com.java.admin.dto.auth.request.LogoutRequestDto;
import com.java.admin.dto.auth.request.RefreshTokenRequestDto;
import com.java.admin.dto.auth.response.LoginResponseDto;
import com.java.admin.dto.auth.response.LogoutResponseDto;
import com.java.admin.dto.auth.response.RefreshTokenResponseDto;
import com.java.admin.entity.auth.RefreshTokenEntity;
import com.java.admin.entity.user.UserEntity;
import com.java.admin.exception.auth.AuthException;
import com.java.admin.security.JwtService;
import com.java.admin.service.auth.AuthUserServiceImpl;
import com.java.admin.service.auth.UserDetailServiceImpl;
import com.java.admin.usecase.auth.IBlacklistedTokenService;
import com.java.admin.usecase.auth.IRefreshTokenService;
import com.java.admin.security.CustomAuthUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthUserServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserDetailServiceImpl userDetailsService;

    @Mock
    private JwtService jwtService;

    @Mock
    private IRefreshTokenService refreshTokenService;

    @Mock
    private IBlacklistedTokenService blacklistedTokenService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthUserServiceImpl authUserService;

    private LoginRequestDto loginRequest;
    private LogoutRequestDto logoutRequest;
    private RefreshTokenRequestDto refreshTokenRequest;
    private CustomAuthUser customAuthUser;
    private RefreshTokenEntity refreshTokenEntity;

    private static final String VALID_EMAIL = "test@example.com";
    private static final String VALID_PASSWORD = "password123";
    private static final String VALID_TOKEN = "valid.jwt.token";
    private static final String VALID_REFRESH_TOKEN = "valid.refresh.token";
    private static final String NEW_ACCESS_TOKEN = "new.access.token";
    private static final String NEW_REFRESH_TOKEN = "new.refresh.token";

    @BeforeEach
    void setUp() {
        loginRequest = new LoginRequestDto(VALID_EMAIL, VALID_PASSWORD);
        logoutRequest = new LogoutRequestDto("Bearer " + VALID_TOKEN);
        refreshTokenRequest = new RefreshTokenRequestDto(VALID_REFRESH_TOKEN);

        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setEmail(VALID_EMAIL);
        userEntity.setPassword("$2a$10$encodedPassword");
        userEntity.setEnabled(true);
        userEntity.setAccountNonExpired(true);
        userEntity.setAccountNonLocked(true);
        userEntity.setCredentialsNonExpired(true);

        customAuthUser = new CustomAuthUser(
                1L,
                VALID_EMAIL,
                "$2a$10$encodedPassword",
                true,
                true,
                true,
                true,
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        refreshTokenEntity = new RefreshTokenEntity();
        refreshTokenEntity.setToken(VALID_REFRESH_TOKEN);
        refreshTokenEntity.setUserEmail(VALID_EMAIL);
        refreshTokenEntity.setExpiryAt(OffsetDateTime.now().plusSeconds(7200));
    }

    @Test
    void login_shouldReturnTokens_whenValidCredentials() {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(userDetailsService.loadUserByUsername(VALID_EMAIL)).thenReturn(customAuthUser);
        when(jwtService.generateToken(customAuthUser)).thenReturn(VALID_TOKEN);
        when(refreshTokenService.createRefreshToken(VALID_EMAIL)).thenReturn(refreshTokenEntity);

        // Act
        LoginResponseDto response = authUserService.login(loginRequest);

        // Assert
        assertNotNull(response);
        assertEquals(VALID_TOKEN, response.token());
        assertEquals(VALID_REFRESH_TOKEN, response.refreshToken());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userDetailsService).loadUserByUsername(VALID_EMAIL);
        verify(jwtService).generateToken(customAuthUser);
        verify(refreshTokenService).createRefreshToken(VALID_EMAIL);
    }

    @Test
    void login_shouldThrowAuthException_whenAuthenticationFails() {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(false);

        // Act & Assert
        AuthException exception = assertThrows(AuthException.class,
                () -> authUserService.login(loginRequest));

        assertEquals(AuthException.AUTHENTICATION_FAILED_CODE, exception.getErrorCode());
        assertEquals(AuthException.AUTHENTICATION_FAILED_MESSAGE, exception.getErrorMessage());
        assertEquals(AuthException.AUTHENTICATION_FAILED_CAUSE, exception.getErrorCause());
        verify(userDetailsService, never()).loadUserByUsername(anyString());
        verify(jwtService, never()).generateToken(any());
        verify(refreshTokenService, never()).createRefreshToken(anyString());
    }

    @Test
    void logout_shouldReturnSuccessMessage_whenValidToken() {
        // Arrange
        when(jwtService.extractUsername(VALID_TOKEN)).thenReturn(VALID_EMAIL);
        doNothing().when(blacklistedTokenService).blacklistToken(VALID_TOKEN);
        doNothing().when(refreshTokenService).deleteByUserEmail(VALID_EMAIL);

        // Act
        LogoutResponseDto response = authUserService.logout(logoutRequest);

        // Assert
        assertNotNull(response);
        assertTrue(response.message().contains("logged out successfully"));
        verify(blacklistedTokenService).blacklistToken(VALID_TOKEN);
        verify(jwtService).extractUsername(VALID_TOKEN);
        verify(refreshTokenService).deleteByUserEmail(VALID_EMAIL);
    }

    @Test
    void logout_shouldThrowAuthException_whenInvalidTokenFormat() {
        // Arrange
        LogoutRequestDto invalidRequest = new LogoutRequestDto("InvalidTokenFormat");

        // Act & Assert
        AuthException exception = assertThrows(AuthException.class,
                () -> authUserService.logout(invalidRequest));

        assertEquals(AuthException.INVALID_TOKEN_CODE, exception.getErrorCode());
        assertEquals(AuthException.INVALID_TOKEN_MESSAGE, exception.getErrorMessage());
        assertEquals(AuthException.INVALID_TOKEN_CAUSE, exception.getErrorCause());
        verify(blacklistedTokenService, never()).blacklistToken(anyString());
        verify(refreshTokenService, never()).deleteByUserEmail(anyString());
    }

    @Test
    void refreshToken_shouldReturnNewTokens_whenValidRefreshToken() {
        // Arrange
        RefreshTokenEntity newRefreshToken = new RefreshTokenEntity();
        newRefreshToken.setToken(NEW_REFRESH_TOKEN);
        newRefreshToken.setUserEmail(VALID_EMAIL);

        when(refreshTokenService.findByToken(VALID_REFRESH_TOKEN)).thenReturn(Optional.of(refreshTokenEntity));
        when(userDetailsService.loadUserByUsername(VALID_EMAIL)).thenReturn(customAuthUser);
        when(jwtService.generateToken(customAuthUser)).thenReturn(NEW_ACCESS_TOKEN);
        when(refreshTokenService.createRefreshToken(VALID_EMAIL)).thenReturn(newRefreshToken);
        doNothing().when(refreshTokenService).deleteByUserEmail(VALID_EMAIL);

        // Act
        RefreshTokenResponseDto response = authUserService.refreshToken(refreshTokenRequest);

        // Assert
        assertNotNull(response);
        assertEquals(NEW_ACCESS_TOKEN, response.token());
        assertEquals(NEW_REFRESH_TOKEN, response.refreshToken());
        verify(refreshTokenService).findByToken(VALID_REFRESH_TOKEN);
        verify(userDetailsService).loadUserByUsername(VALID_EMAIL);
        verify(jwtService).generateToken(customAuthUser);
        verify(refreshTokenService).createRefreshToken(VALID_EMAIL);
        verify(refreshTokenService).deleteByUserEmail(VALID_EMAIL);
    }

    @Test
    void refreshToken_shouldThrowAuthException_whenRefreshTokenNotFound() {
        // Arrange
        when(refreshTokenService.findByToken(VALID_REFRESH_TOKEN)).thenReturn(Optional.empty());

        // Act & Assert
        AuthException exception = assertThrows(AuthException.class,
                () -> authUserService.refreshToken(refreshTokenRequest));

        assertEquals(AuthException.REFRESH_TOKEN_INVALID_CODE, exception.getErrorCode());
        assertEquals(AuthException.REFRESH_TOKEN_INVALID_MESSAGE, exception.getErrorMessage());
        assertEquals(AuthException.REFRESH_TOKEN_INVALID_CAUSE, exception.getErrorCause());
        verify(userDetailsService, never()).loadUserByUsername(anyString());
        verify(jwtService, never()).generateToken(any());
        verify(refreshTokenService, never()).createRefreshToken(anyString());
    }

    @Test
    void refreshToken_shouldThrowAuthException_whenRefreshTokenExpired() {
        // Arrange
        refreshTokenEntity.setExpiryAt(OffsetDateTime.now().minusSeconds(3600));
        when(refreshTokenService.findByToken(VALID_REFRESH_TOKEN)).thenReturn(Optional.of(refreshTokenEntity));

        // Act & Assert
        AuthException exception = assertThrows(AuthException.class,
                () -> authUserService.refreshToken(refreshTokenRequest));

        assertEquals(AuthException.REFRESH_TOKEN_INVALID_CODE, exception.getErrorCode());
        assertEquals(AuthException.REFRESH_TOKEN_INVALID_MESSAGE, exception.getErrorMessage());
        assertEquals(AuthException.REFRESH_TOKEN_INVALID_CAUSE, exception.getErrorCause());
        verify(userDetailsService, never()).loadUserByUsername(anyString());
        verify(jwtService, never()).generateToken(any());
        verify(refreshTokenService, never()).createRefreshToken(anyString());
    }
}