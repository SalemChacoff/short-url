package com.java.admin.unit.service.url;

import com.java.admin.dto.ApiPaginationResponse;
import com.java.admin.dto.url.request.CreateUrlRequestDto;
import com.java.admin.dto.url.request.GetUrlsRequestDto;
import com.java.admin.dto.url.request.PatchUrlRequestDto;
import com.java.admin.dto.url.request.PutUrlRequestDto;
import com.java.admin.dto.url.response.CreateUrlResponseDto;
import com.java.admin.dto.url.response.DeleteUrlResponseDto;
import com.java.admin.dto.url.response.PatchUrlResponseDto;
import com.java.admin.dto.url.response.PutUrlResponseDto;
import com.java.admin.entity.url.UrlEntity;
import com.java.admin.entity.user.UserEntity;
import com.java.admin.exception.url.UrlException;
import com.java.admin.mapper.url.UrlMapper;
import com.java.admin.repository.url.UrlRepository;
import com.java.admin.repository.url.specification.UrlSpecification;
import com.java.admin.repository.user.UserRepository;
import com.java.admin.service.url.UrlServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UrlServiceImplTest {

    @Mock
    private UrlRepository urlRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UrlMapper urlMapper;

    @InjectMocks
    private UrlServiceImpl urlService;

    private CreateUrlRequestDto createUrlRequest;
    private UrlEntity urlEntity;
    private UserEntity userEntity;
    private GetUrlsRequestDto getUrlsRequest;
    private PatchUrlRequestDto patchUrlRequest;
    private PutUrlRequestDto putUrlRequest;
    private CreateUrlResponseDto createUrlResponse;
    private PatchUrlResponseDto patchUrlResponse;
    private PutUrlResponseDto putUrlResponse;

    private static final Long VALID_USER_ID = 1L;
    private static final Long VALID_URL_ID = 1L;
    private static final String VALID_URL = "https://www.example.com";
    private static final String VALID_CUSTOM_ALIAS = "mylink";
    private static final String VALID_DESCRIPTION = "Example URL for testing";
    private static final String VALID_SHORT_URL = "http://short.url/mylink";

    @BeforeEach
    void setUp() {
        OffsetDateTime validSince = OffsetDateTime.now();
        OffsetDateTime validUntil = validSince.plusDays(30);

        createUrlRequest = new CreateUrlRequestDto(
                VALID_CUSTOM_ALIAS,
                VALID_URL,
                VALID_DESCRIPTION,
                validSince,
                validUntil,
                true
        );

        userEntity = new UserEntity();
        userEntity.setId(VALID_USER_ID);
        userEntity.setEmail("test@example.com");

        urlEntity = new UrlEntity();
        urlEntity.setId(VALID_URL_ID);
        urlEntity.setOriginalUrl(VALID_URL);
        urlEntity.setCustomAlias(VALID_CUSTOM_ALIAS);
        urlEntity.setDescription(VALID_DESCRIPTION);
        urlEntity.setShortUrl(VALID_SHORT_URL);
        urlEntity.setActive(true);
        urlEntity.setClickCount(0L);
        urlEntity.setValidSince(validSince);
        urlEntity.setValidUntil(validUntil);
        urlEntity.setUser(userEntity);
        urlEntity.setDeleted(false);

        createUrlResponse = new CreateUrlResponseDto(
                VALID_URL_ID,
                VALID_SHORT_URL,
                VALID_URL,
                VALID_DESCRIPTION,
                true,
                0L,
                validSince,
                validUntil
        );

        getUrlsRequest = new GetUrlsRequestDto(
                VALID_USER_ID,
                1,
                10,
                "createdAt",
                false,
                null,
                null,
                null,
                null,
                null
        );

        patchUrlRequest = new PatchUrlRequestDto(
                validSince,
                validUntil,
                false
        );

        patchUrlResponse = new PatchUrlResponseDto(
                VALID_URL_ID,
                validSince,
                validUntil,
                false
        );

        putUrlRequest = new PutUrlRequestDto(
                "newAlias",
                "https://new-example.com",
                "Updated description",
                validSince,
                validUntil,
                true
        );

        putUrlResponse = new PutUrlResponseDto(
                VALID_URL_ID,
                VALID_SHORT_URL,
                "https://new-example.com",
                "newAlias",
                "Updated description",
                true,
                0L,
                validSince,
                validUntil
        );
    }

    @Test
    void createUrl_shouldCreateUrlSuccessfully_whenValidRequest() {
        // Arrange
        when(urlRepository.findUrlEntitiesByUserAndDeletedIsFalseAndOriginalUrl(VALID_USER_ID, VALID_URL))
                .thenReturn(Optional.empty());
        when(userRepository.findById(VALID_USER_ID)).thenReturn(Optional.of(userEntity));
        when(urlRepository.save(any(UrlEntity.class))).thenReturn(urlEntity);
        when(urlMapper.toResponseDto(any(UrlEntity.class))).thenReturn(createUrlResponse);

        // Act
        CreateUrlResponseDto response = urlService.createUrl(createUrlRequest, VALID_USER_ID);

        // Assert
        assertNotNull(response);
        assertEquals(VALID_URL_ID, response.id());
        assertEquals(VALID_SHORT_URL, response.shortUrl());
        assertEquals(VALID_URL, response.originalUrl());
        verify(urlRepository).findUrlEntitiesByUserAndDeletedIsFalseAndOriginalUrl(VALID_USER_ID, VALID_URL);
        verify(userRepository).findById(VALID_USER_ID);
        verify(urlRepository, times(2)).save(any(UrlEntity.class));
        verify(urlMapper).toResponseDto(any(UrlEntity.class));
    }

    @Test
    void createUrl_shouldThrowUrlException_whenUrlAlreadyExists() {
        // Arrange
        when(urlRepository.findUrlEntitiesByUserAndDeletedIsFalseAndOriginalUrl(VALID_USER_ID, VALID_URL))
                .thenReturn(Optional.of(urlEntity));

        // Act & Assert
        UrlException exception = assertThrows(UrlException.class,
                () -> urlService.createUrl(createUrlRequest, VALID_USER_ID));

        assertEquals(UrlException.URL_ALREADY_EXISTS_CODE, exception.getErrorCode());
        assertEquals(UrlException.URL_ALREADY_EXISTS_MESSAGE, exception.getErrorMessage());
        assertEquals(UrlException.URL_ALREADY_EXISTS_CAUSE, exception.getErrorCause());
        verify(urlRepository, never()).save(any(UrlEntity.class));
        verify(userRepository, never()).findById(any());
    }

    @Test
    void createUrl_shouldThrowRuntimeException_whenUserNotFound() {
        // Arrange
        when(urlRepository.findUrlEntitiesByUserAndDeletedIsFalseAndOriginalUrl(VALID_USER_ID, VALID_URL))
                .thenReturn(Optional.empty());
        when(userRepository.findById(VALID_USER_ID)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> urlService.createUrl(createUrlRequest, VALID_USER_ID));

        assertTrue(exception.getMessage().contains("User not found with ID: " + VALID_USER_ID));
        verify(urlRepository, never()).save(any(UrlEntity.class));
    }

    @Test
    void getAllUrlsByUserId_shouldReturnPaginatedResults_whenUrlsExist() {
        // Arrange
        List<UrlEntity> urlEntities = List.of(urlEntity);
        Page<UrlEntity> urlPage = new PageImpl<>(urlEntities, PageRequest.of(0, 10), 1);
        List<CreateUrlResponseDto> mappedUrls = List.of(createUrlResponse);

        when(urlRepository.findAll(any(UrlSpecification.class), any(Pageable.class))).thenReturn(urlPage);
        when(urlMapper.toResponseDtoList(urlEntities)).thenReturn(mappedUrls);

        // Act
        ApiPaginationResponse response = urlService.getAllUrlsByUserId(getUrlsRequest, VALID_USER_ID);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.totalElements());
        assertEquals(1, response.totalPages());
        assertEquals(1, response.pageNumber());
        assertEquals(10, response.pageSize());
        assertTrue(response.isLastPage());
        assertTrue(response.isFirstPage());
        assertFalse(response.hasNextPage());
        assertFalse(response.hasPreviousPage());
        assertNotNull(response.data());
        verify(urlRepository).findAll(any(UrlSpecification.class), any(Pageable.class));
        verify(urlMapper).toResponseDtoList(urlEntities);
    }

    @Test
    void getAllUrlsByUserId_shouldReturnEmptyResponse_whenNoUrlsFound() {
        // Arrange
        Page<UrlEntity> emptyPage = new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 10), 0);
        when(urlRepository.findAll(any(UrlSpecification.class), any(Pageable.class))).thenReturn(emptyPage);

        // Act
        ApiPaginationResponse response = urlService.getAllUrlsByUserId(getUrlsRequest, VALID_USER_ID);

        // Assert
        assertNotNull(response);
        assertEquals(0L, response.totalElements());
        assertEquals(0, response.totalPages());
        assertEquals(1, response.pageNumber());
        assertEquals(10, response.pageSize());
        assertTrue(response.isLastPage());
        assertTrue(response.isFirstPage());
        assertFalse(response.hasNextPage());
        assertFalse(response.hasPreviousPage());
        assertNull(response.data());
        verify(urlRepository).findAll(any(UrlSpecification.class), any(Pageable.class));
        verify(urlMapper, never()).toResponseDtoList(any());
    }

    @Test
    void toggleUrlStatus_shouldToggleStatusSuccessfully_whenUrlExists() {
        // Arrange
        when(urlRepository.findByIdAndUserIdAndNotDeleted(VALID_URL_ID, VALID_USER_ID))
                .thenReturn(Optional.of(urlEntity));
        when(urlRepository.toggleUrlStatus(VALID_URL_ID, VALID_USER_ID, false)).thenReturn(1);
        when(urlMapper.toPatchResponseDto(urlEntity)).thenReturn(patchUrlResponse);

        // Act
        PatchUrlResponseDto response = urlService.toggleUrlStatus(patchUrlRequest, VALID_URL_ID, VALID_USER_ID);

        // Assert
        assertNotNull(response);
        assertEquals(VALID_URL_ID, response.id());
        assertFalse(response.isActive());
        verify(urlRepository).findByIdAndUserIdAndNotDeleted(VALID_URL_ID, VALID_USER_ID);
        verify(urlRepository).toggleUrlStatus(VALID_URL_ID, VALID_USER_ID, false);
        verify(urlMapper).toPatchResponseDto(urlEntity);
    }

    @Test
    void toggleUrlStatus_shouldThrowUrlException_whenUrlNotFound() {
        // Arrange
        when(urlRepository.findByIdAndUserIdAndNotDeleted(VALID_URL_ID, VALID_USER_ID))
                .thenReturn(Optional.empty());

        // Act & Assert
        UrlException exception = assertThrows(UrlException.class,
                () -> urlService.toggleUrlStatus(patchUrlRequest, VALID_URL_ID, VALID_USER_ID));

        assertEquals(UrlException.URL_NOT_FOUND_CODE, exception.getErrorCode());
        assertEquals(UrlException.URL_NOT_FOUND_MESSAGE, exception.getErrorMessage());
        assertEquals(UrlException.URL_NOT_FOUND_CAUSE, exception.getErrorCause());
        verify(urlRepository, never()).toggleUrlStatus(anyLong(), anyLong(), anyBoolean());
    }

    @Test
    void toggleUrlStatus_shouldThrowUrlException_whenUpdateFails() {
        // Arrange
        when(urlRepository.findByIdAndUserIdAndNotDeleted(VALID_URL_ID, VALID_USER_ID))
                .thenReturn(Optional.of(urlEntity));
        when(urlRepository.toggleUrlStatus(VALID_URL_ID, VALID_USER_ID, false)).thenReturn(0);

        // Act & Assert
        UrlException exception = assertThrows(UrlException.class,
                () -> urlService.toggleUrlStatus(patchUrlRequest, VALID_URL_ID, VALID_USER_ID));

        assertEquals(UrlException.URL_STATUS_UPDATE_FAILED_CODE, exception.getErrorCode());
        assertEquals(UrlException.URL_STATUS_UPDATE_FAILED_MESSAGE, exception.getErrorMessage());
        assertEquals(UrlException.URL_STATUS_UPDATE_FAILED_CAUSE, exception.getErrorCause());
    }

    @Test
    void updateUrl_shouldUpdateUrlSuccessfully_whenUrlExists() {
        // Arrange
        when(urlRepository.findByIdAndUserIdAndNotDeleted(VALID_URL_ID, VALID_USER_ID))
                .thenReturn(Optional.of(urlEntity));
        when(urlRepository.updateUrl(eq(VALID_URL_ID), eq(VALID_USER_ID), anyString(), anyString(),
                anyString(), any(OffsetDateTime.class), any(OffsetDateTime.class), anyBoolean()))
                .thenReturn(1);
        when(urlMapper.toPutResponseDto(urlEntity)).thenReturn(putUrlResponse);

        // Act
        PutUrlResponseDto response = urlService.updateUrl(putUrlRequest, VALID_URL_ID, VALID_USER_ID);

        // Assert
        assertNotNull(response);
        assertEquals(VALID_URL_ID, response.id());
        assertEquals("newAlias", response.customAlias());
        assertEquals("https://new-example.com", response.originalUrl());
        verify(urlRepository).findByIdAndUserIdAndNotDeleted(VALID_URL_ID, VALID_USER_ID);
        verify(urlRepository).updateUrl(eq(VALID_URL_ID), eq(VALID_USER_ID), anyString(), anyString(),
                anyString(), any(OffsetDateTime.class), any(OffsetDateTime.class), anyBoolean());
        verify(urlMapper).toPutResponseDto(urlEntity);
    }

    @Test
    void updateUrl_shouldThrowUrlException_whenUrlNotFound() {
        // Arrange
        when(urlRepository.findByIdAndUserIdAndNotDeleted(VALID_URL_ID, VALID_USER_ID))
                .thenReturn(Optional.empty());

        // Act & Assert
        UrlException exception = assertThrows(UrlException.class,
                () -> urlService.updateUrl(putUrlRequest, VALID_URL_ID, VALID_USER_ID));

        assertEquals(UrlException.URL_NOT_FOUND_CODE, exception.getErrorCode());
        assertEquals(UrlException.URL_NOT_FOUND_MESSAGE, exception.getErrorMessage());
        assertEquals(UrlException.URL_NOT_FOUND_CAUSE, exception.getErrorCause());
        verify(urlRepository, never()).updateUrl(anyLong(), anyLong(), anyString(), anyString(),
                anyString(), any(), any(), anyBoolean());
    }

    @Test
    void updateUrl_shouldThrowUrlException_whenUpdateFails() {
        // Arrange
        when(urlRepository.findByIdAndUserIdAndNotDeleted(VALID_URL_ID, VALID_USER_ID))
                .thenReturn(Optional.of(urlEntity));
        when(urlRepository.updateUrl(eq(VALID_URL_ID), eq(VALID_USER_ID), anyString(), anyString(),
                anyString(), any(OffsetDateTime.class), any(OffsetDateTime.class), anyBoolean()))
                .thenReturn(0);

        // Act & Assert
        UrlException exception = assertThrows(UrlException.class,
                () -> urlService.updateUrl(putUrlRequest, VALID_URL_ID, VALID_USER_ID));

        assertEquals(UrlException.URL_UPDATE_FAILED_CODE, exception.getErrorCode());
        assertEquals(UrlException.URL_UPDATE_FAILED_MESSAGE, exception.getErrorMessage());
        assertEquals(UrlException.URL_UPDATE_FAILED_CAUSE, exception.getErrorCause());
    }

    @Test
    void deleteUrl_shouldDeleteUrlSuccessfully_whenUrlExists() {
        // Arrange
        when(urlRepository.findByIdAndUserIdAndNotDeleted(VALID_URL_ID, VALID_USER_ID))
                .thenReturn(Optional.of(urlEntity));
        when(urlRepository.softDeleteUrl(VALID_URL_ID, VALID_USER_ID)).thenReturn(1);

        // Act
        DeleteUrlResponseDto response = urlService.deleteUrl(VALID_URL_ID, VALID_USER_ID);

        // Assert
        assertNotNull(response);
        assertTrue(response.isDeleted());
        verify(urlRepository).findByIdAndUserIdAndNotDeleted(VALID_URL_ID, VALID_USER_ID);
        verify(urlRepository).softDeleteUrl(VALID_URL_ID, VALID_USER_ID);
    }

    @Test
    void deleteUrl_shouldThrowUrlException_whenUrlNotFound() {
        // Arrange
        when(urlRepository.findByIdAndUserIdAndNotDeleted(VALID_URL_ID, VALID_USER_ID))
                .thenReturn(Optional.empty());

        // Act & Assert
        UrlException exception = assertThrows(UrlException.class,
                () -> urlService.deleteUrl(VALID_URL_ID, VALID_USER_ID));

        assertEquals(UrlException.URL_NOT_FOUND_CODE, exception.getErrorCode());
        assertEquals(UrlException.URL_NOT_FOUND_MESSAGE, exception.getErrorMessage());
        assertEquals(UrlException.URL_NOT_FOUND_CAUSE, exception.getErrorCause());
        verify(urlRepository, never()).softDeleteUrl(anyLong(), anyLong());
    }

    @Test
    void deleteUrl_shouldThrowUrlException_whenDeleteFails() {
        // Arrange
        when(urlRepository.findByIdAndUserIdAndNotDeleted(VALID_URL_ID, VALID_USER_ID))
                .thenReturn(Optional.of(urlEntity));
        when(urlRepository.softDeleteUrl(VALID_URL_ID, VALID_USER_ID)).thenReturn(0);

        // Act & Assert
        UrlException exception = assertThrows(UrlException.class,
                () -> urlService.deleteUrl(VALID_URL_ID, VALID_USER_ID));

        assertEquals(UrlException.URL_DELETE_FAILED_CODE, exception.getErrorCode());
        assertEquals(UrlException.URL_DELETE_FAILED_MESSAGE, exception.getErrorMessage());
        assertEquals(UrlException.URL_DELETE_FAILED_CAUSE, exception.getErrorCause());
    }

    @Test
    void toggleUrlStatus_shouldUseExistingStatus_whenPatchRequestIsActiveIsNull() {
        // Arrange
        PatchUrlRequestDto requestWithNullActive = new PatchUrlRequestDto(
                OffsetDateTime.now(),
                OffsetDateTime.now().plusDays(30),
                null
        );
        urlEntity.setActive(true);

        when(urlRepository.findByIdAndUserIdAndNotDeleted(VALID_URL_ID, VALID_USER_ID))
                .thenReturn(Optional.of(urlEntity));
        when(urlRepository.toggleUrlStatus(VALID_URL_ID, VALID_USER_ID, true)).thenReturn(1);
        when(urlMapper.toPatchResponseDto(urlEntity)).thenReturn(patchUrlResponse);

        // Act
        PatchUrlResponseDto response = urlService.toggleUrlStatus(requestWithNullActive, VALID_URL_ID, VALID_USER_ID);

        // Assert
        assertNotNull(response);
        verify(urlRepository).toggleUrlStatus(VALID_URL_ID, VALID_USER_ID, true);
    }
}