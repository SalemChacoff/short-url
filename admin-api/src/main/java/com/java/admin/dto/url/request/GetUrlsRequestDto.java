package com.java.admin.dto.url.request;

import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.OffsetDateTime;

public record GetUrlsRequestDto(

        // Validación de ID de usuario (ya existente pero corregida)
        Long userId,

        // Validación de paginación (ya existente pero corregida)
        @NotNull(message = "Page cannot be null")
        @Min(value = 0, message = "Page must be a non-negative number")
        Integer page,

        @NotNull(message = "Size cannot be null")
        @Min(value = 1, message = "Size must be at least 1")
        @Max(value = 100, message = "Size must be at most 100")
        Integer size,

        // Validación para el campo de ordenamiento
        @Pattern(regexp = "^(createdAt|originalUrl|shortUrl|visits|isActive|validSince|validUntil)$",
                message = "Sort field must be one of: createdAt, originalUrl, shortUrl, visits, isActive, validSince, validUntil")
        String sortBy,

        // Dirección de ordenamiento
        Boolean ascending,

        // Campo por el que se filtra
        @Pattern(regexp = "^(originalUrl|shortUrl|description|customAlias)$",
                message = "Filter field must be one of: originalUrl, shortUrl, isActive, description, customAlias")
        String filterBy,

        // Valor del filtro (podría requerir validación específica según filterBy)
        String filterValue,

        // Estado activo/inactivo
        Boolean isActive,

        // Fechas de validez
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        OffsetDateTime validSince,

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        OffsetDateTime validUntil
) {
    // Constructor que valida la relación entre fechas
    public GetUrlsRequestDto {
        // Validación condicional: si ambas fechas están presentes, validSince debe ser anterior a validUntil
        if (validSince != null && validUntil != null && validSince.isAfter(validUntil)) {
            throw new IllegalArgumentException("validSince must be before validUntil");
        }
    }
}