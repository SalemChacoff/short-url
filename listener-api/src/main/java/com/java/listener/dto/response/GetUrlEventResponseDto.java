package com.java.listener.dto.response;

public record GetUrlEventResponseDto(
        Boolean success,
        String url
) {
}
