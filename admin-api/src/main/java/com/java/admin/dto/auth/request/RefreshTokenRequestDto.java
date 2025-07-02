package com.java.admin.dto.auth.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RefreshTokenRequestDto (
        @NotNull(message = "Refresh token cannot be null")
        @NotBlank(message = "Refresh token cannot be blank")
        @Size(min = 10, max = 100, message = "Refresh token must be between 10 and 100 characters")
        String refreshToken
) { }