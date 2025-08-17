package com.java.listener.dto.request;

public record GetUrlEventRequestDto(
        String shortUrl,
        String eventType,
        String timestamp,
        String ipAddress
) {
}
