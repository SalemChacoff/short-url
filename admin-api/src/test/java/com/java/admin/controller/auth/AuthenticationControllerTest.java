package com.java.admin.controller.auth;

import com.java.admin.dto.auth.request.LoginRequestDto;
import com.java.admin.dto.auth.response.LoginResponseDto;
import com.java.admin.security.JwtService;
import com.java.admin.service.auth.UserDetailServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserDetailServiceImpl userDetailsService;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthenticationController authenticationController;

    private LoginRequestDto validRequest;
    private UserDetails userDetails;
    private static final String TEST_EMAIL = "usuario@ejemplo.com";
    private static final String TEST_PASSWORD = "password123";
    private static final String TEST_TOKEN = "jwt.token.string";

    @BeforeEach
    void setUp() {
        // Configurar request válido
        validRequest = new LoginRequestDto(TEST_EMAIL, TEST_PASSWORD);

        // Crear UserDetails mock
        userDetails = new User(TEST_EMAIL, TEST_PASSWORD, Collections.emptyList());
    }

    @Test
    @DisplayName("Debería autenticar al usuario y devolver un token JWT")
    void authenticateSuccess() {
        // Configurar mocks
        when(userDetailsService.loadUserByUsername(TEST_EMAIL)).thenReturn(userDetails);
        when(jwtService.generateToken(userDetails)).thenReturn(TEST_TOKEN);

        // Ejecutar método bajo prueba
        ResponseEntity<LoginResponseDto> response = authenticationController.authenticate(validRequest);

        // Verificaciones
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(TEST_TOKEN, response.getBody().token());

        // Verificar interacciones
        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken(TEST_EMAIL, TEST_PASSWORD));
        verify(userDetailsService).loadUserByUsername(TEST_EMAIL);
        verify(jwtService).generateToken(userDetails);
    }

    @Test
    @DisplayName("Debería lanzar excepción cuando las credenciales son incorrectas")
    void authenticateWithInvalidCredentials() {
        // Configurar mock para lanzar excepción
        doThrow(new BadCredentialsException("Credenciales inválidas"))
                .when(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        // Verificar que se lanza la excepción
        assertThrows(BadCredentialsException.class, () ->
                authenticationController.authenticate(validRequest));

        // Verificar que no se llegó a llamar a estos métodos
        verify(userDetailsService, never()).loadUserByUsername(anyString());
        verify(jwtService, never()).generateToken(any());
    }
}