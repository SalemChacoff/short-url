package com.java.admin.dto.auth.response;

public record RefreshTokenResponseDto(
        String token,
        String refreshToken
) {
}
