package com.java.admin.dto.url.response;

import java.time.OffsetDateTime;

public record PatchUrlResponseDto(
        Long id,
        OffsetDateTime validSince,
        OffsetDateTime validUntil,
        Boolean isActive
) {
}
