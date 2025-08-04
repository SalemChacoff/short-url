package com.java.listener.dto.request;

public record ListenerUrlEventRequestDto(
        String shortUrl,
        String eventType,
        String timestamp,
        String ipAddress
) {
}
