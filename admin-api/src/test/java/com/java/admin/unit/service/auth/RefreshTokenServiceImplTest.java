package com.java.admin.unit.service.auth;

import com.java.admin.config.JwtPropertiesConfig;
import com.java.admin.dto.auth.request.RefreshTokenRequestDto;
import com.java.admin.dto.auth.response.RefreshTokenResponseDto;
import com.java.admin.entity.auth.RefreshTokenEntity;
import com.java.admin.exception.auth.AuthException;
import com.java.admin.repository.auth.RefreshTokenRepository;
import com.java.admin.security.JwtService;
import com.java.admin.service.auth.RefreshTokenServiceImpl;
import com.java.admin.service.auth.UserDetailServiceImpl;
import com.java.admin.security.CustomAuthUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceImplTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private JwtPropertiesConfig jwtPropertiesConfig;

    @Mock
    private UserDetailServiceImpl userDetailsService;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private RefreshTokenServiceImpl refreshTokenService;

    private RefreshTokenRequestDto refreshTokenRequest;
    private RefreshTokenEntity refreshTokenEntity;
    private CustomAuthUser customAuthUser;

    private static final String VALID_EMAIL = "test@example.com";
    private static final String VALID_REFRESH_TOKEN = "valid-refresh-token";
    private static final String INVALID_REFRESH_TOKEN = "invalid-refresh-token";
    private static final String NEW_ACCESS_TOKEN = "new.access.token";
    private static final String NEW_REFRESH_TOKEN = "new-refresh-token";
    private static final long REFRESH_TOKEN_EXPIRATION = 604800000L; // 7 days

    @BeforeEach
    void setUp() {
        refreshTokenRequest = new RefreshTokenRequestDto(VALID_REFRESH_TOKEN);

        refreshTokenEntity = new RefreshTokenEntity();
        refreshTokenEntity.setId(1L);
        refreshTokenEntity.setToken(VALID_REFRESH_TOKEN);
        refreshTokenEntity.setUserEmail(VALID_EMAIL);
        refreshTokenEntity.setExpiryAt(OffsetDateTime.now().plusSeconds(7200));
        refreshTokenEntity.setCreatedAt(OffsetDateTime.now());
        refreshTokenEntity.setUpdatedAt(OffsetDateTime.now());

        customAuthUser = new CustomAuthUser(
                1L,
                VALID_EMAIL,
                "password",
                true,
                true,
                true,
                true,
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }

    @Test
    void refreshToken_shouldReturnNewTokens_whenValidRefreshTokenProvided() {
        // Arrange
        RefreshTokenEntity newRefreshTokenEntity = new RefreshTokenEntity();
        newRefreshTokenEntity.setToken(NEW_REFRESH_TOKEN);
        newRefreshTokenEntity.setUserEmail(VALID_EMAIL);
        newRefreshTokenEntity.setExpiryAt(OffsetDateTime.now().plusSeconds(7200));

        when(refreshTokenRepository.findByToken(VALID_REFRESH_TOKEN)).thenReturn(Optional.of(refreshTokenEntity));
        when(userDetailsService.loadUserByUsername(VALID_EMAIL)).thenReturn(customAuthUser);
        when(jwtService.generateToken(customAuthUser)).thenReturn(NEW_ACCESS_TOKEN);
        when(jwtPropertiesConfig.getRefreshTokenExpiration()).thenReturn(REFRESH_TOKEN_EXPIRATION);
        when(refreshTokenRepository.findByUserEmail(VALID_EMAIL)).thenReturn(Optional.empty());
        when(refreshTokenRepository.save(any(RefreshTokenEntity.class))).thenReturn(newRefreshTokenEntity);

        // Act
        RefreshTokenResponseDto result = refreshTokenService.refreshToken(refreshTokenRequest);

        // Assert
        assertNotNull(result);
        assertEquals(NEW_ACCESS_TOKEN, result.token());
        assertEquals(NEW_REFRESH_TOKEN, result.refreshToken());

        verify(refreshTokenRepository).findByToken(VALID_REFRESH_TOKEN);
        verify(userDetailsService).loadUserByUsername(VALID_EMAIL);
        verify(jwtService).generateToken(customAuthUser);
        verify(refreshTokenRepository).delete(refreshTokenEntity);
        verify(refreshTokenRepository).save(any(RefreshTokenEntity.class));
    }

    @Test
    void refreshToken_shouldThrowAuthException_whenRefreshTokenNotFound() {
        // Arrange
        when(refreshTokenRepository.findByToken(INVALID_REFRESH_TOKEN)).thenReturn(Optional.empty());
        RefreshTokenRequestDto invalidRequest = new RefreshTokenRequestDto(INVALID_REFRESH_TOKEN);

        // Act & Assert
        AuthException exception = assertThrows(AuthException.class,
                () -> refreshTokenService.refreshToken(invalidRequest));

        assertEquals(AuthException.REFRESH_TOKEN_INVALID_CODE, exception.getErrorCode());
        assertEquals(AuthException.REFRESH_TOKEN_INVALID_MESSAGE, exception.getErrorMessage());
        assertEquals(AuthException.REFRESH_TOKEN_INVALID_CAUSE, exception.getErrorCause());

        verify(refreshTokenRepository).findByToken(INVALID_REFRESH_TOKEN);
        verify(userDetailsService, never()).loadUserByUsername(any());
        verify(jwtService, never()).generateToken(any());
    }

    @Test
    void refreshToken_shouldThrowAuthException_whenRefreshTokenExpired() {
        // Arrange
        refreshTokenEntity.setExpiryAt(OffsetDateTime.now().minusSeconds(3600)); // Token expirado
        when(refreshTokenRepository.findByToken(VALID_REFRESH_TOKEN)).thenReturn(Optional.of(refreshTokenEntity));

        // Act & Assert
        AuthException exception = assertThrows(AuthException.class,
                () -> refreshTokenService.refreshToken(refreshTokenRequest));

        assertEquals(AuthException.REFRESH_TOKEN_INVALID_CODE, exception.getErrorCode());
        assertEquals(AuthException.REFRESH_TOKEN_INVALID_MESSAGE, exception.getErrorMessage());
        assertEquals(AuthException.REFRESH_TOKEN_INVALID_CAUSE, exception.getErrorCause());

        verify(refreshTokenRepository).findByToken(VALID_REFRESH_TOKEN);
        verify(refreshTokenRepository).delete(refreshTokenEntity);
        verify(userDetailsService, never()).loadUserByUsername(any());
        verify(jwtService, never()).generateToken(any());
    }

    @Test
    void createRefreshToken_shouldCreateNewToken_whenValidEmailProvided() {
        // Arrange
        ArgumentCaptor<RefreshTokenEntity> entityCaptor = ArgumentCaptor.forClass(RefreshTokenEntity.class);
        RefreshTokenEntity savedEntity = new RefreshTokenEntity();
        savedEntity.setId(1L);
        savedEntity.setToken("generated-token");
        savedEntity.setUserEmail(VALID_EMAIL);
        savedEntity.setExpiryAt(OffsetDateTime.now().plusSeconds(7200));

        when(jwtPropertiesConfig.getRefreshTokenExpiration()).thenReturn(REFRESH_TOKEN_EXPIRATION);
        when(refreshTokenRepository.findByUserEmail(VALID_EMAIL)).thenReturn(Optional.empty());
        when(refreshTokenRepository.save(any(RefreshTokenEntity.class))).thenReturn(savedEntity);

        // Act
        RefreshTokenEntity result = refreshTokenService.createRefreshToken(VALID_EMAIL);

        // Assert
        assertNotNull(result);
        assertEquals(VALID_EMAIL, result.getUserEmail());

        verify(refreshTokenRepository).save(entityCaptor.capture());
        RefreshTokenEntity capturedEntity = entityCaptor.getValue();

        assertEquals(VALID_EMAIL, capturedEntity.getUserEmail());
        assertNotNull(capturedEntity.getToken());
        assertNotNull(capturedEntity.getExpiryAt());
        assertTrue(capturedEntity.getExpiryAt().isAfter(OffsetDateTime.now()));
    }

    @Test
    void createRefreshToken_shouldDeleteExistingToken_whenUserAlreadyHasRefreshToken() {
        // Arrange
        RefreshTokenEntity existingToken = new RefreshTokenEntity();
        existingToken.setUserEmail(VALID_EMAIL);

        RefreshTokenEntity newToken = new RefreshTokenEntity();
        newToken.setId(2L);
        newToken.setUserEmail(VALID_EMAIL);

        when(jwtPropertiesConfig.getRefreshTokenExpiration()).thenReturn(REFRESH_TOKEN_EXPIRATION);
        when(refreshTokenRepository.findByUserEmail(VALID_EMAIL)).thenReturn(Optional.of(existingToken));
        when(refreshTokenRepository.save(any(RefreshTokenEntity.class))).thenReturn(newToken);

        // Act
        RefreshTokenEntity result = refreshTokenService.createRefreshToken(VALID_EMAIL);

        // Assert
        assertNotNull(result);
        verify(refreshTokenRepository).findByUserEmail(VALID_EMAIL);
        verify(refreshTokenRepository).delete(existingToken);
        verify(refreshTokenRepository).save(any(RefreshTokenEntity.class));
    }

    @Test
    void findByToken_shouldReturnToken_whenTokenExists() {
        // Arrange
        when(refreshTokenRepository.findByToken(VALID_REFRESH_TOKEN)).thenReturn(Optional.of(refreshTokenEntity));

        // Act
        Optional<RefreshTokenEntity> result = refreshTokenService.findByToken(VALID_REFRESH_TOKEN);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(refreshTokenEntity, result.get());
        verify(refreshTokenRepository).findByToken(VALID_REFRESH_TOKEN);
    }

    @Test
    void findByToken_shouldReturnEmpty_whenTokenDoesNotExist() {
        // Arrange
        when(refreshTokenRepository.findByToken(INVALID_REFRESH_TOKEN)).thenReturn(Optional.empty());

        // Act
        Optional<RefreshTokenEntity> result = refreshTokenService.findByToken(INVALID_REFRESH_TOKEN);

        // Assert
        assertFalse(result.isPresent());
        verify(refreshTokenRepository).findByToken(INVALID_REFRESH_TOKEN);
    }

    @Test
    void verifyExpiration_shouldReturnToken_whenTokenNotExpired() {
        // Arrange
        refreshTokenEntity.setExpiryAt(OffsetDateTime.now().plusSeconds(7200));

        // Act
        RefreshTokenEntity result = refreshTokenService.verifyExpiration(refreshTokenEntity);

        // Assert
        assertEquals(refreshTokenEntity, result);
        verify(refreshTokenRepository, never()).delete(any());
    }

    @Test
    void verifyExpiration_shouldThrowException_whenTokenExpired() {
        // Arrange
        refreshTokenEntity.setExpiryAt(OffsetDateTime.now().minusSeconds(3600));

        // Act & Assert
        AuthException exception = assertThrows(AuthException.class,
                () -> refreshTokenService.verifyExpiration(refreshTokenEntity));

        assertEquals(AuthException.REFRESH_TOKEN_INVALID_CODE, exception.getErrorCode());
        assertEquals(AuthException.REFRESH_TOKEN_INVALID_MESSAGE, exception.getErrorMessage());
        assertEquals(AuthException.REFRESH_TOKEN_INVALID_CAUSE, exception.getErrorCause());

        verify(refreshTokenRepository).delete(refreshTokenEntity);
    }

    @Test
    void deleteByUserEmail_shouldCallRepository_whenValidEmailProvided() {
        // Act
        refreshTokenService.deleteByUserEmail(VALID_EMAIL);

        // Assert
        verify(refreshTokenRepository).deleteByUserEmail(VALID_EMAIL);
    }
}