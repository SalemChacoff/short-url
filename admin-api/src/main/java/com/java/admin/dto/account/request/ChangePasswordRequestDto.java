package com.java.admin.dto.account.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ChangePasswordRequestDto(
        @NotNull(message = "Email cannot be null")
        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Email should be valid")
        String email,
        @NotNull(message = "Reset password token cannot be null")
        @NotBlank(message = "Reset password token cannot be blank")
        @Size(min = 10, max = 100, message = "Reset password token must be between 10 and 100 characters")
        String resetPasswordToken,
        @NotNull(message = "Reset password verificationCode cannot be null")
        @NotBlank(message = "Reset password verificationCode cannot be blank")
        @Size(min = 6, max = 6, message = "Reset password verificationCode must be exactly 6 characters")
        String resetPasswordCode,
        @NotNull(message = "Password cannot be null")
        @NotBlank(message = "Password cannot be blank")
        @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
        String newPassword
) {
}
