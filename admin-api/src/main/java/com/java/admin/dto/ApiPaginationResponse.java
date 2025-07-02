package com.java.admin.dto;

public record ApiPaginationResponse(
        long totalElements,
        int totalPages,
        int pageNumber,
        int pageSize,
        boolean isLastPage,
        boolean isFirstPage,
        boolean hasNextPage,
        boolean hasPreviousPage,
        Object data
) {
}
