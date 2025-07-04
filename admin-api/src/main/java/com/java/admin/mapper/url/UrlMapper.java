package com.java.admin.mapper.url;

import com.java.admin.dto.url.response.CreateUrlResponseDto;
import com.java.admin.dto.url.response.PatchUrlResponseDto;
import com.java.admin.dto.url.response.PutUrlResponseDto;
import com.java.admin.entity.url.UrlEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UrlMapper {

    public CreateUrlResponseDto toResponseDto(UrlEntity urlEntity) {
        if (urlEntity == null) {
            return null;
        }

        return new CreateUrlResponseDto(
                urlEntity.getId(),
                urlEntity.getOriginalUrl(),
                urlEntity.getShortUrl(),
                urlEntity.getDescription(),
                urlEntity.isActive(),
                urlEntity.getClickCount(),
                urlEntity.getValidSince(),
                urlEntity.getValidUntil()
        );
    }

    public List<CreateUrlResponseDto> toResponseDtoList(List<UrlEntity> urlEntities) {
        if (urlEntities == null || urlEntities.isEmpty()) {
            return List.of();
        }

        return urlEntities.stream()
                .map(this::toResponseDto)
                .toList();
    }

    public PatchUrlResponseDto toPatchResponseDto(UrlEntity urlEntity) {
        if (urlEntity == null) {
            return null;
        }

        return new PatchUrlResponseDto(
                urlEntity.getId(),
                urlEntity.getValidSince(),
                urlEntity.getValidUntil(),
                urlEntity.isActive()
        );
    }

    public PutUrlResponseDto toPutResponseDto(UrlEntity urlEntity) {
        if (urlEntity == null) {
            return null;
        }

        return new PutUrlResponseDto(
                urlEntity.getId(),
                urlEntity.getShortUrl(),
                urlEntity.getOriginalUrl(),
                urlEntity.getCustomAlias(),
                urlEntity.getDescription(),
                urlEntity.isActive(),
                urlEntity.getClickCount(),
                urlEntity.getValidSince(),
                urlEntity.getValidUntil()
        );
    }

}
