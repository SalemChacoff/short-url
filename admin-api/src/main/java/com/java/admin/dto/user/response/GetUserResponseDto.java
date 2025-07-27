package com.java.admin.dto.user.response;

public record GetUserResponseDto(
        String username,
        String email,
        String firstName,
        String lastName,
        String phoneNumber,
        String address
) {
}
