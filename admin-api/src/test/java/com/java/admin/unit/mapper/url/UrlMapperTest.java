package com.java.admin.unit.mapper.url;

import com.java.admin.dto.url.response.CreateUrlResponseDto;
import com.java.admin.dto.url.response.PatchUrlResponseDto;
import com.java.admin.dto.url.response.PutUrlResponseDto;
import com.java.admin.entity.url.UrlEntity;
import com.java.admin.mapper.url.UrlMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UrlMapper Tests")
class UrlMapperTest {

    private static final Long VALID_ID = 1L;
    private static final String VALID_ORIGINAL_URL = "https://www.example.com";
    private static final String VALID_SHORT_URL = "http://short.ly/abc123";
    private static final String VALID_CUSTOM_ALIAS = "mylink";
    private static final String VALID_DESCRIPTION = "Example URL description";
    private static final boolean VALID_ACTIVE = true;
    private static final Long VALID_CLICK_COUNT = 100L;
    private static final OffsetDateTime VALID_SINCE = OffsetDateTime.now().minusDays(1);
    private static final OffsetDateTime VALID_UNTIL = OffsetDateTime.now().plusDays(30);

    @InjectMocks
    private UrlMapper urlMapper;

    private UrlEntity urlEntity;

    @BeforeEach
    void setUp() {
        urlEntity = new UrlEntity();
        urlEntity.setId(VALID_ID);
        urlEntity.setOriginalUrl(VALID_ORIGINAL_URL);
        urlEntity.setShortUrl(VALID_SHORT_URL);
        urlEntity.setCustomAlias(VALID_CUSTOM_ALIAS);
        urlEntity.setDescription(VALID_DESCRIPTION);
        urlEntity.setActive(VALID_ACTIVE);
        urlEntity.setClickCount(VALID_CLICK_COUNT);
        urlEntity.setValidSince(VALID_SINCE);
        urlEntity.setValidUntil(VALID_UNTIL);
    }

    @Test
    @DisplayName("Should map UrlEntity to CreateUrlResponseDto correctly")
    void toResponseDto_shouldMapCorrectly_whenValidUrlEntity() {
        // Act
        CreateUrlResponseDto result = urlMapper.toResponseDto(urlEntity);

        // Assert
        assertNotNull(result);
        assertEquals(VALID_ID, result.id());
        assertEquals(VALID_ORIGINAL_URL, result.originalUrl());
        assertEquals(VALID_SHORT_URL, result.shortUrl());
        assertEquals(VALID_DESCRIPTION, result.description());
        assertEquals(VALID_ACTIVE, result.isActive());
        assertEquals(VALID_CLICK_COUNT, result.clickCount());
        assertEquals(VALID_SINCE, result.validSince());
        assertEquals(VALID_UNTIL, result.validUntil());
    }

    @Test
    @DisplayName("Should return null when UrlEntity is null for CreateUrlResponseDto")
    void toResponseDto_shouldReturnNull_whenUrlEntityIsNull() {
        // Act
        CreateUrlResponseDto result = urlMapper.toResponseDto(null);

        // Assert
        assertNull(result);
    }

    @Test
    @DisplayName("Should map list of UrlEntity to list of CreateUrlResponseDto correctly")
    void toResponseDtoList_shouldMapCorrectly_whenValidUrlEntityList() {
        // Arrange
        UrlEntity secondUrlEntity = new UrlEntity();
        secondUrlEntity.setId(2L);
        secondUrlEntity.setOriginalUrl("https://www.google.com");
        secondUrlEntity.setShortUrl("http://short.ly/xyz789");
        secondUrlEntity.setDescription("Google URL");
        secondUrlEntity.setActive(false);
        secondUrlEntity.setClickCount(50L);
        secondUrlEntity.setValidSince(VALID_SINCE);
        secondUrlEntity.setValidUntil(VALID_UNTIL);

        List<UrlEntity> urlEntities = List.of(urlEntity, secondUrlEntity);

        // Act
        List<CreateUrlResponseDto> result = urlMapper.toResponseDtoList(urlEntities);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());

        CreateUrlResponseDto firstResult = result.getFirst();
        assertEquals(VALID_ID, firstResult.id());
        assertEquals(VALID_ORIGINAL_URL, firstResult.originalUrl());

        CreateUrlResponseDto secondResult = result.get(1);
        assertEquals(2L, secondResult.id());
        assertEquals("https://www.google.com", secondResult.originalUrl());
    }

    @Test
    @DisplayName("Should return empty list when UrlEntity list is null")
    void toResponseDtoList_shouldReturnEmptyList_whenUrlEntityListIsNull() {
        // Act
        List<CreateUrlResponseDto> result = urlMapper.toResponseDtoList(null);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should return empty list when UrlEntity list is empty")
    void toResponseDtoList_shouldReturnEmptyList_whenUrlEntityListIsEmpty() {
        // Act
        List<CreateUrlResponseDto> result = urlMapper.toResponseDtoList(Collections.emptyList());

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should map UrlEntity to PatchUrlResponseDto correctly")
    void toPatchResponseDto_shouldMapCorrectly_whenValidUrlEntity() {
        // Act
        PatchUrlResponseDto result = urlMapper.toPatchResponseDto(urlEntity);

        // Assert
        assertNotNull(result);
        assertEquals(VALID_ID, result.id());
        assertEquals(VALID_SINCE, result.validSince());
        assertEquals(VALID_UNTIL, result.validUntil());
        assertEquals(VALID_ACTIVE, result.isActive());
    }

    @Test
    @DisplayName("Should return null when UrlEntity is null for PatchUrlResponseDto")
    void toPatchResponseDto_shouldReturnNull_whenUrlEntityIsNull() {
        // Act
        PatchUrlResponseDto result = urlMapper.toPatchResponseDto(null);

        // Assert
        assertNull(result);
    }

    @Test
    @DisplayName("Should map UrlEntity to PutUrlResponseDto correctly")
    void toPutResponseDto_shouldMapCorrectly_whenValidUrlEntity() {
        // Act
        PutUrlResponseDto result = urlMapper.toPutResponseDto(urlEntity);

        // Assert
        assertNotNull(result);
        assertEquals(VALID_ID, result.id());
        assertEquals(VALID_SHORT_URL, result.shortUrl());
        assertEquals(VALID_ORIGINAL_URL, result.originalUrl());
        assertEquals(VALID_CUSTOM_ALIAS, result.customAlias());
        assertEquals(VALID_DESCRIPTION, result.description());
        assertEquals(VALID_ACTIVE, result.isActive());
        assertEquals(VALID_CLICK_COUNT, result.clickCount());
        assertEquals(VALID_SINCE, result.validSince());
        assertEquals(VALID_UNTIL, result.validUntil());
    }

    @Test
    @DisplayName("Should return null when UrlEntity is null for PutUrlResponseDto")
    void toPutResponseDto_shouldReturnNull_whenUrlEntityIsNull() {
        // Act
        PutUrlResponseDto result = urlMapper.toPutResponseDto(null);

        // Assert
        assertNull(result);
    }

    @Test
    @DisplayName("Should handle UrlEntity with null fields correctly")
    void toResponseDto_shouldHandleNullFields_whenUrlEntityHasNullValues() {
        // Arrange
        UrlEntity entityWithNulls = new UrlEntity();
        entityWithNulls.setId(VALID_ID);
        entityWithNulls.setActive(false);
        entityWithNulls.setClickCount(0L);

        // Act
        CreateUrlResponseDto result = urlMapper.toResponseDto(entityWithNulls);

        // Assert
        assertNotNull(result);
        assertEquals(VALID_ID, result.id());
        assertNull(result.originalUrl());
        assertNull(result.shortUrl());
        assertNull(result.description());
        assertFalse(result.isActive());
        assertEquals(0L, result.clickCount());
        assertNull(result.validSince());
        assertNull(result.validUntil());
    }
}