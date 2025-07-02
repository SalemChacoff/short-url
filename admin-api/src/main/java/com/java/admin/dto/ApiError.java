package com.java.admin.dto;

public record ApiError (
        Integer errorCode,
        String errorMessage,
        String errorCause
) { }
