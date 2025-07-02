package com.java.admin.dto.auth.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LogoutRequestDto(
        @NotNull(message = "Token cannot be null")
        @NotBlank(message = "Token cannot be blank")
        @Size(min = 10, max = 100, message = "Token must be between 10 and 100 characters")
        String token
) {
}
