package com.java.admin.dto;


import java.util.List;

public record ApiResponseDto(
        List<ApiError> errors,
        boolean success,
        Object data
) { }
