package com.java.admin.unit.controller.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.admin.constant.ApiAuthEndpoints;
import com.java.admin.controller.auth.AuthenticationController;
import com.java.admin.dto.auth.request.LoginRequestDto;
import com.java.admin.dto.auth.request.LogoutRequestDto;
import com.java.admin.dto.auth.request.RefreshTokenRequestDto;
import com.java.admin.dto.auth.response.LoginResponseDto;
import com.java.admin.dto.auth.response.LogoutResponseDto;
import com.java.admin.dto.auth.response.RefreshTokenResponseDto;
import com.java.admin.usecase.auth.IAuthUserService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

    private static final String VALID_EMAIL = "usuario@ejemplo.com";
    private static final String VALID_PASSWORD = "Password123!";
    private static final String VALID_TOKEN = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...";
    private static final String VALID_REFRESH_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...";

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private IAuthUserService authUserService;

    @InjectMocks
    private AuthenticationController authenticationController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authenticationController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void authenticate_shouldReturnOk() throws Exception {
        // Arrange
        LoginRequestDto requestDto = new LoginRequestDto(VALID_EMAIL, VALID_PASSWORD);
        LoginResponseDto responseDto = new LoginResponseDto(VALID_TOKEN, VALID_REFRESH_TOKEN);
        when(authUserService.login(any(LoginRequestDto.class))).thenReturn(responseDto);

        // Act
        ResultActions resultActions = mockMvc.perform(post(ApiAuthEndpoints.BASE_PATH + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));

        // Assert
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.token").value(VALID_TOKEN))
                .andExpect(jsonPath("$.data.refreshToken").value(VALID_REFRESH_TOKEN));

        verify(authUserService, times(1)).login(any(LoginRequestDto.class));
    }

    @Test
    void logout_shouldReturnOk() throws Exception {
        // Arrange
        LogoutResponseDto responseDto = new LogoutResponseDto("User logged out successfully");
        when(authUserService.logout(any(LogoutRequestDto.class))).thenReturn(responseDto);

        // Act
        ResultActions resultActions = mockMvc.perform(post(ApiAuthEndpoints.BASE_PATH + "/logout")
                .header("Authorization", VALID_TOKEN));

        // Assert
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.message").value("User logged out successfully"));

        verify(authUserService, times(1)).logout(any(LogoutRequestDto.class));
    }

    @Test
    void refreshToken_shouldReturnOk() throws Exception {
        // Arrange
        RefreshTokenRequestDto requestDto = new RefreshTokenRequestDto(VALID_REFRESH_TOKEN);
        RefreshTokenResponseDto responseDto = new RefreshTokenResponseDto(VALID_TOKEN, VALID_REFRESH_TOKEN);
        when(authUserService.refreshToken(any(RefreshTokenRequestDto.class))).thenReturn(responseDto);

        // Act
        ResultActions resultActions = mockMvc.perform(post(ApiAuthEndpoints.BASE_PATH + "/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));

        // Assert
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.token").value(VALID_TOKEN))
                .andExpect(jsonPath("$.data.refreshToken").value(VALID_REFRESH_TOKEN));

        verify(authUserService, times(1)).refreshToken(any(RefreshTokenRequestDto.class));
    }
}