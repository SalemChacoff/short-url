package com.java.admin.unit.service.auth;

import com.java.admin.entity.auth.BlacklistedTokenEntity;
import com.java.admin.repository.auth.BlacklistedTokenRepository;
import com.java.admin.service.auth.BlacklistedTokenServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BlacklistedTokenServiceImplTest {

    @Mock
    private BlacklistedTokenRepository blacklistedTokenRepository;

    @InjectMocks
    private BlacklistedTokenServiceImpl blacklistedTokenService;

    private static final String VALID_TOKEN = "valid.jwt.token";
    private static final String BLACKLISTED_TOKEN = "blacklisted.jwt.token";

    @BeforeEach
    void setUp() {
        // Setup common test data if needed
    }

    @Test
    void blacklistToken_shouldSaveTokenEntity_whenValidTokenProvided() {
        // Arrange
        ArgumentCaptor<BlacklistedTokenEntity> entityCaptor = ArgumentCaptor.forClass(BlacklistedTokenEntity.class);

        // Act
        blacklistedTokenService.blacklistToken(VALID_TOKEN);

        // Assert
        verify(blacklistedTokenRepository).save(entityCaptor.capture());
        BlacklistedTokenEntity savedEntity = entityCaptor.getValue();

        assertNotNull(savedEntity);
        assertEquals(VALID_TOKEN, savedEntity.getToken());
        assertNotNull(savedEntity.getExpiryAt());
        assertTrue(savedEntity.getExpiryAt().isBefore(OffsetDateTime.now().plusMinutes(1)));
        assertTrue(savedEntity.getExpiryAt().isAfter(OffsetDateTime.now().minusMinutes(1)));
    }

    @Test
    void isTokenBlacklisted_shouldReturnTrue_whenTokenIsBlacklisted() {
        // Arrange
        when(blacklistedTokenRepository.existsByToken(BLACKLISTED_TOKEN)).thenReturn(true);

        // Act
        boolean result = blacklistedTokenService.isTokenBlacklisted(BLACKLISTED_TOKEN);

        // Assert
        assertTrue(result);
        verify(blacklistedTokenRepository).existsByToken(BLACKLISTED_TOKEN);
    }

    @Test
    void isTokenBlacklisted_shouldReturnFalse_whenTokenIsNotBlacklisted() {
        // Arrange
        when(blacklistedTokenRepository.existsByToken(VALID_TOKEN)).thenReturn(false);

        // Act
        boolean result = blacklistedTokenService.isTokenBlacklisted(VALID_TOKEN);

        // Assert
        assertFalse(result);
        verify(blacklistedTokenRepository).existsByToken(VALID_TOKEN);
    }

    @Test
    void cleanupExpiredTokens_shouldCallRepository_whenInvoked() {
        // Arrange
        ArgumentCaptor<OffsetDateTime> dateCaptor = ArgumentCaptor.forClass(OffsetDateTime.class);

        // Act
        blacklistedTokenService.cleanupExpiredTokens();

        // Assert
        verify(blacklistedTokenRepository).deleteByExpiryAtBefore(dateCaptor.capture());
        OffsetDateTime capturedDate = dateCaptor.getValue();

        assertNotNull(capturedDate);
        assertTrue(capturedDate.isBefore(OffsetDateTime.now().plusMinutes(1)));
        assertTrue(capturedDate.isAfter(OffsetDateTime.now().minusMinutes(1)));
    }

    @Test
    void cleanupExpiredTokens_shouldDeleteExpiredTokens_whenExpiredTokensExist() {
        // Act
        blacklistedTokenService.cleanupExpiredTokens();

        // Assert
        verify(blacklistedTokenRepository).deleteByExpiryAtBefore(any(OffsetDateTime.class));
    }
}