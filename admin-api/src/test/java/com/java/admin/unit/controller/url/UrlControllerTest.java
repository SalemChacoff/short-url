package com.java.admin.unit.controller.url;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.admin.constant.ApiUrlEndpoints;
import com.java.admin.controller.url.UrlController;
import com.java.admin.dto.ApiPaginationResponse;
import com.java.admin.dto.url.request.CreateUrlRequestDto;
import com.java.admin.dto.url.request.GetUrlsRequestDto;
import com.java.admin.dto.url.request.PatchUrlRequestDto;
import com.java.admin.dto.url.request.PutUrlRequestDto;
import com.java.admin.dto.url.response.CreateUrlResponseDto;
import com.java.admin.dto.url.response.DeleteUrlResponseDto;
import com.java.admin.dto.url.response.GetUrlsResponseDto;
import com.java.admin.dto.url.response.PatchUrlResponseDto;
import com.java.admin.dto.url.response.PutUrlResponseDto;
import com.java.admin.usecase.url.IUrlService;
import com.java.admin.security.CustomAuthUser;
import org.jetbrains.annotations.NotNull;
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
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.time.OffsetDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UrlControllerTest {

    private static final String VALID_URL = "https://www.example.com";
    private static final String VALID_CUSTOM_ALIAS = "mylink";
    private static final String VALID_DESCRIPTION = "Example URL for testing purposes";
    private static final Long VALID_USER_ID = 1L;
    private static final Long VALID_URL_ID = 1L;
    private static final String VALID_SHORT_URL = "http://short.url/mylink";

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private Authentication authentication;

    @Mock
    private IUrlService urlService;

    @InjectMocks
    private UrlController urlController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(urlController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        // Configure authentication and CustomAuthUser mocks
        authentication = mock(Authentication.class);
        CustomAuthUser customAuthUser = mock(CustomAuthUser.class);

        when(authentication.getPrincipal()).thenReturn(customAuthUser);
        when(customAuthUser.getId()).thenReturn(VALID_USER_ID);
    }

    @Test
    void createShortUrl_shouldReturnCreated() throws Exception {
        // Arrange
        OffsetDateTime validSince = OffsetDateTime.now();
        OffsetDateTime validUntil = validSince.plusDays(30);

        CreateUrlRequestDto requestDto = new CreateUrlRequestDto(
                VALID_CUSTOM_ALIAS,
                VALID_URL,
                VALID_DESCRIPTION,
                validSince,
                validUntil,
                true);

        CreateUrlResponseDto responseDto = new CreateUrlResponseDto(
                VALID_URL_ID,
                VALID_SHORT_URL,
                VALID_URL,
                VALID_DESCRIPTION,
                true,
                0L,
                validSince,
                validUntil);

        when(urlService.createUrl(any(CreateUrlRequestDto.class), eq(VALID_USER_ID))).thenReturn(responseDto);

        // Act
        ResultActions resultActions = mockMvc.perform(post(ApiUrlEndpoints.BASE_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto))
                .principal(authentication));

        // Assert
        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(VALID_URL_ID))
                .andExpect(jsonPath("$.data.shortUrl").value(VALID_SHORT_URL))
                .andExpect(jsonPath("$.data.originalUrl").value(VALID_URL))
                .andExpect(jsonPath("$.data.description").value(VALID_DESCRIPTION))
                .andExpect(jsonPath("$.data.isActive").value(true));

        // Verify
        verify(urlService, times(1)).createUrl(any(CreateUrlRequestDto.class), eq(VALID_USER_ID));
    }

    @Test
    void getAllUrlsByUserId_shouldReturnOk() throws Exception {
        // Arrange
        GetUrlsRequestDto requestDto = new GetUrlsRequestDto(
                null,
                0,
                10,
                "createdAt",
                true,
                null,
                null,
                null,
                null,
                null);

        ApiPaginationResponse paginationResponse = getApiPaginationResponse();

        when(urlService.getAllUrlsByUserId(any(GetUrlsRequestDto.class), eq(VALID_USER_ID)))
                .thenReturn(paginationResponse);

        // Act
        ResultActions resultActions = mockMvc.perform(get(ApiUrlEndpoints.BASE_PATH)
                .content(objectMapper.writeValueAsString(requestDto))
                .param("page", "0")
                .param("size", "10")
                .principal(authentication));

        // Assert
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.totalElements").value(1))
                .andExpect(jsonPath("$.data.pageNumber").value(0))
                .andExpect(jsonPath("$.data.data[0].id").value(VALID_URL_ID))
                .andExpect(jsonPath("$.data.data[0].shortUrl").value(VALID_SHORT_URL))
                .andExpect(jsonPath("$.data.data[0].originalUrl").value(VALID_URL))
                .andExpect(jsonPath("$.data.data[0].customAlias").value(VALID_CUSTOM_ALIAS));

        // Verify
        verify(urlService, times(1)).getAllUrlsByUserId(any(GetUrlsRequestDto.class), eq(VALID_USER_ID));
    }

    private static @NotNull ApiPaginationResponse getApiPaginationResponse() {
        GetUrlsResponseDto responseDto = new GetUrlsResponseDto(
                VALID_URL_ID,
                VALID_SHORT_URL,
                VALID_URL,
                VALID_DESCRIPTION,
                VALID_CUSTOM_ALIAS,
                true,
                "2023-01-01T00:00:00Z",
                "2023-12-31T23:59:59Z");

        List<GetUrlsResponseDto> urlsList = List.of(responseDto);

        return new ApiPaginationResponse(
                1, // totalElements
                1, // totalPages
                0, // pageNumber
                10, // pageSize
                true, // isLastPage
                true, // isFirstPage
                false, // hasNextPage
                false, // hasPreviousPage
                urlsList // data
        );
    }

    @Test
    void toggleUrlStatus_shouldReturnOk() throws Exception {
        // Arrange
        OffsetDateTime validSince = OffsetDateTime.now();
        OffsetDateTime validUntil = validSince.plusDays(30);

        PatchUrlRequestDto requestDto = new PatchUrlRequestDto(validSince, validUntil, false);
        PatchUrlResponseDto responseDto = new PatchUrlResponseDto(VALID_URL_ID, validSince, validUntil, false);

        when(urlService.toggleUrlStatus(any(PatchUrlRequestDto.class), eq(VALID_URL_ID), eq(VALID_USER_ID)))
                .thenReturn(responseDto);

        // Act
        ResultActions resultActions = mockMvc.perform(patch(ApiUrlEndpoints.BASE_PATH + "/{urlId}", VALID_URL_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto))
                .principal(authentication));

        // Assert
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(VALID_URL_ID))
                .andExpect(jsonPath("$.data.isActive").value(false));

        // Verify
        verify(urlService, times(1)).toggleUrlStatus(any(PatchUrlRequestDto.class), eq(VALID_URL_ID), eq(VALID_USER_ID));
    }

    @Test
    void updateUrl_shouldReturnOk() throws Exception {
        // Arrange
        OffsetDateTime validSince = OffsetDateTime.now();
        OffsetDateTime validUntil = validSince.plusDays(30);

        PutUrlRequestDto requestDto = new PutUrlRequestDto(
                VALID_CUSTOM_ALIAS,
                VALID_URL,
                VALID_DESCRIPTION,
                validSince,
                validUntil,
                true);

        PutUrlResponseDto responseDto = new PutUrlResponseDto(
                VALID_URL_ID,
                VALID_SHORT_URL,
                VALID_URL,
                VALID_CUSTOM_ALIAS,
                VALID_DESCRIPTION,
                true,
                5L,
                validSince,
                validUntil);

        when(urlService.updateUrl(any(PutUrlRequestDto.class), eq(VALID_URL_ID), eq(VALID_USER_ID)))
                .thenReturn(responseDto);

        // Act
        ResultActions resultActions = mockMvc.perform(put(ApiUrlEndpoints.BASE_PATH + "/{urlId}", VALID_URL_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto))
                .principal(authentication));

        // Assert
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(VALID_URL_ID))
                .andExpect(jsonPath("$.data.shortUrl").value(VALID_SHORT_URL))
                .andExpect(jsonPath("$.data.originalUrl").value(VALID_URL))
                .andExpect(jsonPath("$.data.customAlias").value(VALID_CUSTOM_ALIAS));

        // Verify
        verify(urlService, times(1)).updateUrl(any(PutUrlRequestDto.class), eq(VALID_URL_ID), eq(VALID_USER_ID));
    }

    @Test
    void deleteUrl_shouldReturnOk() throws Exception {
        // Arrange
        DeleteUrlResponseDto responseDto = new DeleteUrlResponseDto(true);
        when(urlService.deleteUrl(VALID_URL_ID, VALID_USER_ID)).thenReturn(responseDto);

        // Act
        ResultActions resultActions = mockMvc.perform(delete(ApiUrlEndpoints.BASE_PATH + "/{urlId}", VALID_URL_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")
                .principal(authentication));

        // Assert
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.isDeleted").value(true));

        // Verify
        verify(urlService, times(1)).deleteUrl(VALID_URL_ID, VALID_USER_ID);
    }
}