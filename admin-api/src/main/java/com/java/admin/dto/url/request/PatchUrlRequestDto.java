package com.java.admin.dto.url.request;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.OffsetDateTime;

public record PatchUrlRequestDto(
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        OffsetDateTime validSince,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        OffsetDateTime validUntil,
        Boolean isActive
) {
}
