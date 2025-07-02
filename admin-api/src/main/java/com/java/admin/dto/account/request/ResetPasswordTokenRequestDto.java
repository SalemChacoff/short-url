package com.java.admin.dto.account.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ResetPasswordTokenRequestDto(
        @NotNull(message = "Reset password token cannot be null")
        @NotBlank(message = "Reset password token cannot be blank")
        @Size(min = 10, max = 100, message = "Reset password token must be between 10 and 100 characters")
        String resetPasswordToken
) {
}
