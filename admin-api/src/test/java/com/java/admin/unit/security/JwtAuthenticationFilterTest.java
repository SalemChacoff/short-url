package com.java.admin.unit.security;

import com.java.admin.security.JwtAuthenticationFilter;
import com.java.admin.security.JwtService;
import com.java.admin.service.auth.UserDetailServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private UserDetailServiceImpl userDetailsService;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.validtoken";
    private static final String BEARER_TOKEN = "Bearer " + VALID_TOKEN;
    private static final String TEST_EMAIL = "test@example.com";
    private UserDetails userDetails;
    private MockHttpServletRequest request;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);
        userDetails = User.builder()
                .username(TEST_EMAIL)
                .password("password")
                .authorities(List.of(new SimpleGrantedAuthority("ROLE_USER")))
                .build();
        request = new MockHttpServletRequest();
    }

    @Test
    @DisplayName("Should continue filter chain when Authorization header does not start with Bearer")
    void doFilterInternal_shouldContinueFilterChain_whenAuthorizationHeaderNotBearer() throws ServletException, IOException {
        // Arrange
        request.addHeader("Authorization", "Basic dGVzdDp0ZXN0");

        // Act
        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        // Assert
        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(jwtService, userDetailsService);
    }

    @Test
    @DisplayName("Should continue filter chain when JWT token is invalid")
    void doFilterInternal_shouldContinueFilterChain_whenJwtTokenIsInvalid() throws ServletException, IOException {
        // Arrange
        request.addHeader("Authorization", BEARER_TOKEN);
        when(jwtService.extractUsername(VALID_TOKEN)).thenThrow(new RuntimeException("Invalid token"));

        // Act
        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        // Assert
        verify(filterChain).doFilter(request, response);
        verify(jwtService).extractUsername(VALID_TOKEN);
        verifyNoInteractions(userDetailsService);
    }

    @Test
    @DisplayName("Should continue filter chain when username cannot be extracted from token")
    void doFilterInternal_shouldContinueFilterChain_whenUsernameIsNull() throws ServletException, IOException {
        // Arrange
        request.addHeader("Authorization", BEARER_TOKEN);
        when(jwtService.extractUsername(VALID_TOKEN)).thenReturn(null);

        // Act
        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        // Assert
        verify(filterChain).doFilter(request, response);
        verify(jwtService).extractUsername(VALID_TOKEN);
        verifyNoInteractions(userDetailsService);
    }

    @Test
    @DisplayName("Should continue filter chain when user is already authenticated")
    void doFilterInternal_shouldContinueFilterChain_whenUserAlreadyAuthenticated() throws ServletException, IOException {
        // Arrange
        Authentication existingAuth = mock(Authentication.class);
        request.addHeader("Authorization", BEARER_TOKEN);
        when(jwtService.extractUsername(VALID_TOKEN)).thenReturn(TEST_EMAIL);
        when(securityContext.getAuthentication()).thenReturn(existingAuth);

        // Act
        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        // Assert
        verify(filterChain).doFilter(request, response);
        verify(jwtService).extractUsername(VALID_TOKEN);
        verifyNoInteractions(userDetailsService);
        verify(securityContext, never()).setAuthentication(any());
    }

    @Test
    @DisplayName("Should set authentication when token is valid and user is not authenticated")
    void doFilterInternal_shouldSetAuthentication_whenTokenIsValidAndUserNotAuthenticated() throws ServletException, IOException {
        // Arrange
        request.addHeader("Authorization", BEARER_TOKEN);
        when(jwtService.extractUsername(VALID_TOKEN)).thenReturn(TEST_EMAIL);
        when(securityContext.getAuthentication()).thenReturn(null);
        when(userDetailsService.loadUserByUsername(TEST_EMAIL)).thenReturn(userDetails);
        when(jwtService.isTokenValid(VALID_TOKEN, userDetails)).thenReturn(true);

        // Act
        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        // Assert
        verify(filterChain).doFilter(request, response);
        verify(jwtService).extractUsername(VALID_TOKEN);
        verify(userDetailsService).loadUserByUsername(TEST_EMAIL);
        verify(jwtService).isTokenValid(VALID_TOKEN, userDetails);
        verify(securityContext).setAuthentication(any());
    }

    @Test
    @DisplayName("Should not set authentication when token is invalid")
    void doFilterInternal_shouldNotSetAuthentication_whenTokenIsInvalid() throws ServletException, IOException {
        // Arrange
        request.addHeader("Authorization", BEARER_TOKEN);
        when(jwtService.extractUsername(VALID_TOKEN)).thenReturn(TEST_EMAIL);
        when(securityContext.getAuthentication()).thenReturn(null);
        when(userDetailsService.loadUserByUsername(TEST_EMAIL)).thenReturn(userDetails);
        when(jwtService.isTokenValid(VALID_TOKEN, userDetails)).thenReturn(false);

        // Act
        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        // Assert
        verify(filterChain).doFilter(request, response);
        verify(jwtService).extractUsername(VALID_TOKEN);
        verify(userDetailsService).loadUserByUsername(TEST_EMAIL);
        verify(jwtService).isTokenValid(VALID_TOKEN, userDetails);
        verify(securityContext, never()).setAuthentication(any());
    }

    @Test
    @DisplayName("Should not filter auth endpoints")
    void shouldNotFilter_shouldReturnTrue_whenPathIsAuthEndpoint() {
        // Arrange
        request.setServletPath("/api/v1/auth/login");

        // Act
        boolean result = jwtAuthenticationFilter.shouldNotFilter(request);

        // Assert
        assertTrue(result);
    }

    @Test
    @DisplayName("Should not filter swagger endpoints")
    void shouldNotFilter_shouldReturnTrue_whenPathIsSwaggerEndpoint() {
        // Arrange
        request.setServletPath("/swagger-ui/index.html");

        // Act
        boolean result = jwtAuthenticationFilter.shouldNotFilter(request);

        // Assert
        assertTrue(result);
    }

    @Test
    @DisplayName("Should not filter api-docs endpoints")
    void shouldNotFilter_shouldReturnTrue_whenPathIsApiDocsEndpoint() {
        // Arrange
        request.setServletPath("/v3/api-docs/swagger-config");

        // Act
        boolean result = jwtAuthenticationFilter.shouldNotFilter(request);

        // Assert
        assertTrue(result);
    }

    @Test
    @DisplayName("Should filter protected endpoints")
    void shouldNotFilter_shouldReturnFalse_whenPathIsProtectedEndpoint() {
        // Arrange
        request.setServletPath("/api/v1/users");

        // Act
        boolean result = jwtAuthenticationFilter.shouldNotFilter(request);

        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("Should handle UserDetailsService exception gracefully")
    void doFilterInternal_shouldHandleUserDetailsServiceException_gracefully() throws ServletException, IOException {
        // Arrange
        request.addHeader("Authorization", BEARER_TOKEN);
        when(jwtService.extractUsername(VALID_TOKEN)).thenReturn(TEST_EMAIL);
        when(securityContext.getAuthentication()).thenReturn(null);
        when(userDetailsService.loadUserByUsername(TEST_EMAIL))
                .thenThrow(new RuntimeException("User not found"));

        // Act
        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        // Assert
        verify(filterChain).doFilter(request, response);
        verify(jwtService).extractUsername(VALID_TOKEN);
        verify(userDetailsService).loadUserByUsername(TEST_EMAIL);
        verify(securityContext, never()).setAuthentication(any());
    }

    @Test
    @DisplayName("Should handle JWT service exception gracefully during token validation")
    void doFilterInternal_shouldHandleJwtServiceException_gracefully() throws ServletException, IOException {
        // Arrange
        request.addHeader("Authorization", BEARER_TOKEN);
        when(jwtService.extractUsername(VALID_TOKEN)).thenReturn(TEST_EMAIL);
        when(securityContext.getAuthentication()).thenReturn(null);
        when(userDetailsService.loadUserByUsername(TEST_EMAIL)).thenReturn(userDetails);
        when(jwtService.isTokenValid(VALID_TOKEN, userDetails))
                .thenThrow(new RuntimeException("Token validation failed"));

        // Act
        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        // Assert
        verify(filterChain).doFilter(request, response);
        verify(jwtService).extractUsername(VALID_TOKEN);
        verify(userDetailsService).loadUserByUsername(TEST_EMAIL);
        verify(jwtService).isTokenValid(VALID_TOKEN, userDetails);
        verify(securityContext, never()).setAuthentication(any());
    }
}