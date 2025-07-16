package com.java.admin.dto.user.response;

public record UpdateUserResponseDto(
        String username,
        String firstName,
        String lastName,
        String phoneNumber,
        String address
) {
}
