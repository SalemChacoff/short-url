package com.java.admin.dto.account.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record VerifyAccountRequestDto(
        @NotNull(message = "Verification token cannot be null")
        @NotBlank(message = "Verification token cannot be blank")
        @Size(min = 10, max = 100, message = "Verification token must be between 10 and 100 characters")
        String verificationToken
) {
}
