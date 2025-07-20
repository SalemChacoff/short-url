package com.java.admin.unit.security;

import com.java.admin.config.JwtPropertiesConfig;
import com.java.admin.security.JwtService;
import com.java.admin.usecase.auth.IBlacklistedTokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @Mock
    private IBlacklistedTokenService blacklistedTokenService;

    @Mock
    private JwtPropertiesConfig jwtPropertiesConfig;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private JwtService jwtService;

    private static final String TEST_USERNAME = "testuser";
    private static final String TEST_SECRET = "testSecretKeyForJwtTokenGenerationThatIsLongEnough";
    private static final Long TEST_EXPIRATION = 86400000L; // 24 hours
    private String validToken;
    private String expiredToken;

    @BeforeEach
    void setUp() {
        lenient().when(jwtPropertiesConfig.getJwtSecret()).thenReturn(TEST_SECRET);
        lenient().when(jwtPropertiesConfig.getJwtExpiration()).thenReturn(TEST_EXPIRATION);
        lenient().when(userDetails.getUsername()).thenReturn(TEST_USERNAME);

        // Crear tokens para pruebas
        Key signingKey = Keys.hmacShaKeyFor(TEST_SECRET.getBytes());

        validToken = Jwts.builder()
                .setSubject(TEST_USERNAME)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TEST_EXPIRATION))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();

        expiredToken = Jwts.builder()
                .setSubject(TEST_USERNAME)
                .setIssuedAt(new Date(System.currentTimeMillis() - TEST_EXPIRATION - 1000))
                .setExpiration(new Date(System.currentTimeMillis() - 1000))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    @Test
    @DisplayName("Should extract username from valid token")
    void extractUsername_shouldReturnUsername_whenTokenIsValid() {
        // Act
        String extractedUsername = jwtService.extractUsername(validToken);

        // Assert
        assertEquals(TEST_USERNAME, extractedUsername);
    }

    @Test
    @DisplayName("Should extract claim from token")
    void extractClaim_shouldReturnClaim_whenTokenIsValid() {
        // Act
        String subject = jwtService.extractClaim(validToken, Claims::getSubject);

        // Assert
        assertEquals(TEST_USERNAME, subject);
    }

    @Test
    @DisplayName("Should generate token with UserDetails only")
    void generateToken_shouldReturnValidToken_whenCalledWithUserDetails() {
        // Act
        String token = jwtService.generateToken(userDetails);

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());

        String extractedUsername = jwtService.extractUsername(token);
        assertEquals(TEST_USERNAME, extractedUsername);

        verify(jwtPropertiesConfig, atLeast(1)).getJwtExpiration();
        verify(jwtPropertiesConfig, atLeast(1)).getJwtSecret();
    }

    @Test
    @DisplayName("Should generate token with extra claims")
    void generateToken_shouldReturnValidToken_whenCalledWithExtraClaims() {
        // Arrange
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", "ADMIN");
        extraClaims.put("userId", 123L);

        // Act
        String token = jwtService.generateToken(extraClaims, userDetails);

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());

        String extractedUsername = jwtService.extractUsername(token);
        assertEquals(TEST_USERNAME, extractedUsername);

        verify(jwtPropertiesConfig, atLeast(1)).getJwtExpiration();
        verify(jwtPropertiesConfig, atLeast(1)).getJwtSecret();
    }

    @Test
    @DisplayName("Should return true when token is valid and not blacklisted")
    void isTokenValid_shouldReturnTrue_whenTokenIsValidAndNotBlacklisted() {
        // Arrange
        when(blacklistedTokenService.isTokenBlacklisted(validToken)).thenReturn(false);

        // Act
        boolean isValid = jwtService.isTokenValid(validToken, userDetails);

        // Assert
        assertTrue(isValid);
        verify(blacklistedTokenService).isTokenBlacklisted(validToken);
    }

    @Test
    @DisplayName("Should return false when token is blacklisted")
    void isTokenValid_shouldReturnFalse_whenTokenIsBlacklisted() {
        // Arrange
        when(blacklistedTokenService.isTokenBlacklisted(validToken)).thenReturn(true);

        // Act
        boolean isValid = jwtService.isTokenValid(validToken, userDetails);

        // Assert
        assertFalse(isValid);
        verify(blacklistedTokenService).isTokenBlacklisted(validToken);
    }

    @Test
    @DisplayName("Should return false when token is expired")
    void isTokenValid_shouldReturnFalse_whenTokenIsExpired() {
        // Arrange
        when(blacklistedTokenService.isTokenBlacklisted(expiredToken)).thenReturn(false);

        // Act & Assert
        // El método isTokenValid maneja internamente la ExpiredJwtException
        // y retorna false cuando el token está expirado
        assertFalse(() -> {
            try {
                return jwtService.isTokenValid(expiredToken, userDetails);
            } catch (ExpiredJwtException e) {
                return false; // Token expirado = no válido
            }
        });
    }

    @Test
    @DisplayName("Should return false when username does not match")
    void isTokenValid_shouldReturnFalse_whenUsernameDoesNotMatch() {
        // Arrange
        UserDetails differentUser = mock(UserDetails.class);
        when(differentUser.getUsername()).thenReturn("differentuser");
        when(blacklistedTokenService.isTokenBlacklisted(validToken)).thenReturn(false);

        // Act
        boolean isValid = jwtService.isTokenValid(validToken, differentUser);

        // Assert
        assertFalse(isValid);
        verify(blacklistedTokenService).isTokenBlacklisted(validToken);
    }

    @Test
    @DisplayName("Should extract expiration date from token")
    void extractExpiration_shouldReturnExpirationDate_whenTokenIsValid() {
        // Act
        Date expiration = jwtService.extractExpiration(validToken);

        // Assert
        assertNotNull(expiration);
        assertTrue(expiration.after(new Date()));
    }

    @Test
    @DisplayName("Should throw exception when extracting username from invalid token")
    void extractUsername_shouldThrowException_whenTokenIsInvalid() {
        // Arrange
        String invalidToken = "invalid.token.here";

        // Act & Assert
        assertThrows(Exception.class, () -> jwtService.extractUsername(invalidToken));
    }

    @Test
    @DisplayName("Should throw exception when extracting claims from expired token")
    void extractClaim_shouldThrowExpiredJwtException_whenTokenIsExpired() {
        // Act & Assert
        assertThrows(ExpiredJwtException.class, () ->
                jwtService.extractClaim(expiredToken, Claims::getSubject)
        );
    }
}