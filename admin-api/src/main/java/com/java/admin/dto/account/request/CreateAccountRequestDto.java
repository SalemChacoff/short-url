package com.java.admin.dto.account.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateAccountRequestDto(
        @NotNull(message = "Username cannot be null")
        @NotBlank(message = "Username cannot be blank")
        @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
        String username,
        @NotNull(message = "Password cannot be null")
        @NotBlank(message = "Password cannot be blank")
        @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
        String password,
        @NotNull(message = "Email cannot be null")
        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Email should be valid")
        String email,
        @NotNull(message = "First name cannot be null")
        @NotBlank(message = "First name cannot be blank")
        @Size(min = 2, max = 30, message = "First name must be between 2 and 30 characters")
        String firstName,
        @NotNull(message = "Last name cannot be null")
        @NotBlank(message = "Last name cannot be blank")
        @Size(min = 2, max = 30, message = "Last name must be between 2 and 30 characters")
        String lastName,
        @NotNull(message = "Phone number cannot be null")
        @NotBlank(message = "Phone number cannot be blank")
        @Size(min = 10, max = 15, message = "Phone number must be between 10 and 15 characters")
        String phoneNumber,
        @NotNull(message = "Address cannot be null")
        @NotBlank(message = "Address cannot be blank")
        @Size(min = 5, max = 100, message = "Address must be between 5 and 100 characters")
        String address
) {
}
