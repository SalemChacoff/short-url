package com.java.admin.unit.controller.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.java.admin.constant.ApiUserEndpoints;
import com.java.admin.controller.user.UserController;
import com.java.admin.dto.user.request.UpdateUserRequestDto;
import com.java.admin.dto.user.response.GetUserResponseDto;
import com.java.admin.dto.user.response.UpdateUserResponseDto;
import com.java.admin.usecase.user.IUserService;
import com.java.admin.security.CustomAuthUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private static final Long VALID_USER_ID = 1L;
    private static final String VALID_USERNAME = "johndoe232";
    private static final String VALID_EMAIL = "john.doe@example.com";
    private static final String VALID_FIRST_NAME = "John";
    private static final String VALID_LAST_NAME = "Doe";
    private static final String VALID_PHONE_NUMBER = "01234567890";
    private static final String VALID_ADDRESS = "123 Example Street";

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private Authentication authentication;

    @Mock
    private IUserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        // Configure authentication and CustomAuthUser mocks
        authentication = mock(Authentication.class);
        CustomAuthUser customAuthUser = mock(CustomAuthUser.class);

        when(authentication.getPrincipal()).thenReturn(customAuthUser);
        when(customAuthUser.getId()).thenReturn(VALID_USER_ID);
    }

    @Test
    void getUserInfo_shouldReturnOk() throws Exception {
        // Arrange
        GetUserResponseDto responseDto = new GetUserResponseDto(
                VALID_USERNAME,
                VALID_EMAIL,
                VALID_FIRST_NAME,
                VALID_LAST_NAME,
                VALID_PHONE_NUMBER,
                VALID_ADDRESS
        );

        when(userService.getUserById(VALID_USER_ID)).thenReturn(responseDto);

        // Act
        ResultActions resultActions = mockMvc.perform(get(ApiUserEndpoints.BASE_PATH + ApiUserEndpoints.PROFILE_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .principal(authentication));

        // Assert
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.username").value(VALID_USERNAME))
                .andExpect(jsonPath("$.data.firstName").value(VALID_FIRST_NAME))
                .andExpect(jsonPath("$.data.lastName").value(VALID_LAST_NAME))
                .andExpect(jsonPath("$.data.phoneNumber").value(VALID_PHONE_NUMBER))
                .andExpect(jsonPath("$.data.address").value(VALID_ADDRESS));

        // Verify
        verify(userService, times(1)).getUserById(VALID_USER_ID);
    }

    @Test
    void updateUserProfile_shouldReturnOk() throws Exception {
        // Arrange
        UpdateUserRequestDto requestDto = new UpdateUserRequestDto(
                VALID_USERNAME,
                VALID_FIRST_NAME,
                VALID_LAST_NAME,
                VALID_PHONE_NUMBER,
                VALID_ADDRESS
        );

        UpdateUserResponseDto responseDto = new UpdateUserResponseDto(
                VALID_USERNAME,
                VALID_FIRST_NAME,
                VALID_LAST_NAME,
                VALID_PHONE_NUMBER,
                VALID_ADDRESS
        );

        when(userService.updateUser(eq(VALID_USER_ID), any(UpdateUserRequestDto.class))).thenReturn(responseDto);

        // Act
        ResultActions resultActions = mockMvc.perform(post(ApiUserEndpoints.BASE_PATH + ApiUserEndpoints.PROFILE_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto))
                .principal(authentication));

        // Assert
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.username").value(VALID_USERNAME))
                .andExpect(jsonPath("$.data.firstName").value(VALID_FIRST_NAME))
                .andExpect(jsonPath("$.data.lastName").value(VALID_LAST_NAME))
                .andExpect(jsonPath("$.data.phoneNumber").value(VALID_PHONE_NUMBER))
                .andExpect(jsonPath("$.data.address").value(VALID_ADDRESS));

        // Verify
        verify(userService, times(1)).updateUser(eq(VALID_USER_ID), any(UpdateUserRequestDto.class));
    }
}