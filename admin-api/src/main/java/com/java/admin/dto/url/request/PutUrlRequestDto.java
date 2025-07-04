package com.java.admin.dto.url.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.OffsetDateTime;

public record PutUrlRequestDto(
        @NotNull(message = "Custom alias cannot be null")
        @NotBlank(message = "Custom alias cannot be blank")
        @Size(min = 3, max = 20, message = "Custom alias must be between 3 and 20 characters")
        String customAlias,
        @NotNull(message = "URL cannot be null")
        @NotBlank(message = "URL cannot be blank")
        @Size(min = 5, max = 2048, message = "URL must be between 5 and 2048 characters")
        String url,
        @NotNull(message = "Description cannot be null")
        @NotBlank(message = "Description cannot be blank")
        @Size(min = 5, max = 255, message = "Description must be between 5 and 255 characters")
        String description,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        OffsetDateTime validSince,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        OffsetDateTime validUntil,
        Boolean isActive
) {
}
