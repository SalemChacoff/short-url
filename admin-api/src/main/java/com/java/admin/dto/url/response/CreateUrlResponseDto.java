package com.java.admin.dto.url.response;

import java.time.OffsetDateTime;

public record CreateUrlResponseDto(
        Long id,
        String shortUrl,
        String originalUrl,
        String description,
        Boolean isActive,
        Long clickCount,
        OffsetDateTime validSince,
        OffsetDateTime validUntil
) { }
