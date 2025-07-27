package com.java.admin.unit.config;

import com.java.admin.config.CorsConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CorsConfigTest {

    @InjectMocks
    private CorsConfig corsConfig;

    @Test
    void corsConfigurationSource_shouldReturnValidCorsConfiguration() {
        // Act
        CorsConfigurationSource corsConfigurationSource = corsConfig.corsConfigurationSource();

        // Assert
        assertNotNull(corsConfigurationSource);
        assertInstanceOf(UrlBasedCorsConfigurationSource.class, corsConfigurationSource);
    }

    @Test
    void corsConfigurationSource_shouldHaveCorrectAllowedOriginPatterns() {
        // Act
        CorsConfigurationSource corsConfigurationSource = corsConfig.corsConfigurationSource();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/test");
        CorsConfiguration configuration = corsConfigurationSource.getCorsConfiguration(request);

        // Assert
        assertNotNull(configuration);
        assertEquals(List.of("*"), configuration.getAllowedOriginPatterns());
    }

    @Test
    void corsConfigurationSource_shouldHaveCorrectAllowedMethods() {
        // Act
        CorsConfigurationSource corsConfigurationSource = corsConfig.corsConfigurationSource();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/test");
        CorsConfiguration configuration = corsConfigurationSource.getCorsConfiguration(request);

        // Assert
        assertNotNull(configuration);
        assertEquals(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"),
                configuration.getAllowedMethods());
    }

    @Test
    void corsConfigurationSource_shouldHaveCorrectAllowedHeaders() {
        // Act
        CorsConfigurationSource corsConfigurationSource = corsConfig.corsConfigurationSource();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/test");
        CorsConfiguration configuration = corsConfigurationSource.getCorsConfiguration(request);

        // Assert
        assertNotNull(configuration);
        assertEquals(List.of("*"), configuration.getAllowedHeaders());
    }

    @Test
    void corsConfigurationSource_shouldAllowCredentials() {
        // Act
        CorsConfigurationSource corsConfigurationSource = corsConfig.corsConfigurationSource();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/test");
        CorsConfiguration configuration = corsConfigurationSource.getCorsConfiguration(request);

        // Assert
        assertNotNull(configuration);
        assertEquals(Boolean.TRUE, configuration.getAllowCredentials());
    }

    @Test
    void corsConfigurationSource_shouldHaveCorrectExposedHeaders() {
        // Act
        CorsConfigurationSource corsConfigurationSource = corsConfig.corsConfigurationSource();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/test");
        CorsConfiguration configuration = corsConfigurationSource.getCorsConfiguration(request);

        // Assert
        assertNotNull(configuration);
        assertEquals(List.of("Authorization"), configuration.getExposedHeaders());
    }

    @Test
    void corsConfigurationSource_shouldRegisterConfigurationForAllPaths() {
        // Act
        CorsConfigurationSource corsConfigurationSource = corsConfig.corsConfigurationSource();

        MockHttpServletRequest apiRequest = new MockHttpServletRequest();
        apiRequest.setRequestURI("/api/test");

        MockHttpServletRequest anyRequest = new MockHttpServletRequest();
        anyRequest.setRequestURI("/any/path");

        MockHttpServletRequest rootRequest = new MockHttpServletRequest();
        rootRequest.setRequestURI("/");

        // Assert
        assertNotNull(corsConfigurationSource.getCorsConfiguration(apiRequest));
        assertNotNull(corsConfigurationSource.getCorsConfiguration(anyRequest));
        assertNotNull(corsConfigurationSource.getCorsConfiguration(rootRequest));
    }
}