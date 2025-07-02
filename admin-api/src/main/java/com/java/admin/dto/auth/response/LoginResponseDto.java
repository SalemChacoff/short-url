package com.java.admin.dto.auth.response;

public record LoginResponseDto(
        String token,
        String refreshToken
) { }