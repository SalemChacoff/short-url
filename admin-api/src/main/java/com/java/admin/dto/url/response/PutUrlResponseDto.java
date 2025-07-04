package com.java.admin.dto.url.response;

import java.time.OffsetDateTime;

public record PutUrlResponseDto(
        Long id,
        String shortUrl,
        String originalUrl,
        String customAlias,
        String description,
        Boolean isActive,
        Long clickCount,
        OffsetDateTime validSince,
        OffsetDateTime validUntil
) {
}
