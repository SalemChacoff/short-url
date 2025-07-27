package com.java.admin.controller.url;

import com.java.admin.config.CustomLogger;
import com.java.admin.constant.ApiUrlEndpoints;
import com.java.admin.dto.ApiResponseDto;
import com.java.admin.dto.url.request.CreateUrlRequestDto;
import com.java.admin.dto.url.request.GetUrlsRequestDto;
import com.java.admin.dto.url.request.PatchUrlRequestDto;
import com.java.admin.dto.url.request.PutUrlRequestDto;
import com.java.admin.usecase.url.IUrlService;
import com.java.admin.security.CustomAuthUser;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping(ApiUrlEndpoints.BASE_PATH)
@RequiredArgsConstructor
@Tag(name = "Url", description = "Endpoints for URL management")
public class UrlController {

    private final IUrlService urlService;

    @PostMapping(produces = "application/json", consumes = "application/json")
    public ResponseEntity<ApiResponseDto> createShortUrl(
            @Valid @RequestBody CreateUrlRequestDto createUrlRequestDto,
            Authentication authentication) {

        Object principal = authentication.getPrincipal();
        CustomAuthUser getCurrentUserId = (CustomAuthUser) principal;

        CustomLogger.logInfo(UrlController.class, "Creating short URL for user ID: " + getCurrentUserId.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponseDto(new ArrayList<>(),
                        true,
                        urlService.createUrl(createUrlRequestDto, getCurrentUserId.getId()))
        );
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<ApiResponseDto> getAllUrlsByUserId(
            @ModelAttribute GetUrlsRequestDto getUrlsRequestDto,
            Authentication authentication) {

        Object principal = authentication.getPrincipal();
        CustomAuthUser getCurrentUserId = (CustomAuthUser) principal;

        CustomLogger.logInfo(UrlController.class, "Fetching all URLs for user ID: " + getCurrentUserId.getId());

        return ResponseEntity.status(HttpStatus.OK).body(
                        new ApiResponseDto(new ArrayList<>(),
                        true,
                        urlService.getAllUrlsByUserId(getUrlsRequestDto, getCurrentUserId.getId()))
        );
    }

    @PatchMapping(value = "/{urlId}", produces = "application/json", consumes = "application/json")
    public ResponseEntity<ApiResponseDto> toggleUrlStatus(
            @PathVariable Long urlId,
            @Valid@RequestBody PatchUrlRequestDto patchUrlRequestDto,
            Authentication authentication) {

        Object principal = authentication.getPrincipal();
        CustomAuthUser getCurrentUserId = (CustomAuthUser) principal;

        CustomLogger.logInfo(UrlController.class, "Toggling URL status for user ID: " + getCurrentUserId.getId());

        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponseDto(new ArrayList<>(),
                        true,
                        urlService.toggleUrlStatus(patchUrlRequestDto, urlId, getCurrentUserId.getId()))
        );
    }

    @PutMapping(value = "/{urlId}", produces = "application/json", consumes = "application/json")
    public ResponseEntity<ApiResponseDto> updateUrl(
            @PathVariable Long urlId,
            @Valid @RequestBody PutUrlRequestDto putUrlRequestDto,
            Authentication authentication) {

        Object principal = authentication.getPrincipal();
        CustomAuthUser getCurrentUserId = (CustomAuthUser) principal;

        CustomLogger.logInfo(UrlController.class, "Updating URL for user ID: " + getCurrentUserId.getId());

        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponseDto(new ArrayList<>(),
                        true,
                        urlService.updateUrl(putUrlRequestDto, urlId, getCurrentUserId.getId()))
        );
    }

    @DeleteMapping(value = "/{urlId}", produces = "application/json", consumes = "application/json")
    public ResponseEntity<ApiResponseDto> deleteUrl(
            @PathVariable Long urlId,
            Authentication authentication) {

        Object principal = authentication.getPrincipal();
        CustomAuthUser getCurrentUserId = (CustomAuthUser) principal;

        CustomLogger.logInfo(UrlController.class, "Deleting URL for user ID: " + getCurrentUserId.getId());

        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponseDto(new ArrayList<>(),
                        true,
                        urlService.deleteUrl(urlId, getCurrentUserId.getId()))
        );
    }
}
