package com.java.admin.dto.account.response;

public record ResetPasswordTokenResponseDto(
        String message,
        String resetPasswordToken
) {
}
