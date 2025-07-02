package com.java.admin.dto.account.response;

public record VerifyAccountResponseDto(
        String message,
        String verificationToken
) {
}
