package com.java.admin.dto.url.response;

public record GetUrlsResponseDto(
        Long id,
        String shortUrl,
        String originalUrl,
        String description,
        String customAlias,
        Boolean isActive,
        String validSince,
        String validUntil
) {
}
