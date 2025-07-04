package com.java.admin.service.url;

import com.java.admin.config.CustomLogger;
import com.java.admin.dto.ApiPaginationResponse;
import com.java.admin.dto.url.request.CreateUrlRequestDto;
import com.java.admin.dto.url.request.GetUrlsRequestDto;
import com.java.admin.dto.url.request.PatchUrlRequestDto;
import com.java.admin.dto.url.request.PutUrlRequestDto;
import com.java.admin.dto.url.response.CreateUrlResponseDto;
import com.java.admin.dto.url.response.DeleteUrlResponseDto;
import com.java.admin.dto.url.response.PatchUrlResponseDto;
import com.java.admin.dto.url.response.PutUrlResponseDto;
import com.java.admin.entity.url.UrlEntity;
import com.java.admin.entity.user.UserEntity;
import com.java.admin.exception.url.UrlException;
import com.java.admin.mapper.url.UrlMapper;
import com.java.admin.repository.url.UrlRepository;
import com.java.admin.repository.url.specification.UrlSpecification;
import com.java.admin.repository.user.UserRepository;
import com.java.admin.usecase.url.IUrlService;
import com.java.admin.util.GenerateRandomDataUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UrlServiceImpl implements IUrlService {

    private final UrlRepository urlRepository;
    private final UserRepository userRepository;
    private final UrlMapper urlMapper;

    @Override
    public CreateUrlResponseDto createUrl(CreateUrlRequestDto createUrlRequestDto, Long userId) {

        CustomLogger.logInfo(UrlServiceImpl.class, "Creating URL for user ID: " + userId);

        UrlEntity urlEntity = urlRepository.findUrlEntitiesByUserAndDeletedIsFalseAndOriginalUrl(userId, createUrlRequestDto.url()).orElse(null);
        if (urlEntity != null) {
            CustomLogger.logInfo(UrlServiceImpl.class, "URL already exists for user ID: " + userId);
            throw new UrlException(
                    UrlException.URL_ALREADY_EXISTS_CODE,
                    UrlException.URL_ALREADY_EXISTS_MESSAGE,
                    UrlException.URL_ALREADY_EXISTS_CAUSE);
        }

        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        urlEntity = new UrlEntity();
        urlEntity.setOriginalUrl(createUrlRequestDto.url());
        urlEntity.setDescription(createUrlRequestDto.description());
        urlEntity.setUser(userEntity);
        urlEntity.setActive(createUrlRequestDto.isActive());
        urlEntity.setClickCount(0L);
        urlEntity.setValidSince(createUrlRequestDto.validSince());
        urlEntity.setValidUntil(createUrlRequestDto.validUntil());

        // Save the URL entity to the database to generate an ID and use it for the short URL
        UrlEntity newUrlEntity = urlRepository.save(urlEntity);

        // Generate the short URL using the newly created URL entity's ID
        String shortUrl = GenerateRandomDataUtil.generateShortUrl(newUrlEntity.getId());

        newUrlEntity.setShortUrl(shortUrl);

        newUrlEntity = urlRepository.save(newUrlEntity);

        return urlMapper.toResponseDto(newUrlEntity);
    }

    @Override
    public ApiPaginationResponse getAllUrlsByUserId(GetUrlsRequestDto getUrlsRequestDto, Long userId) {

        CustomLogger.logInfo(UrlServiceImpl.class, "Fetching all URLs for user ID: " + userId);

        Pageable pageable = PageRequest.of(getUrlsRequestDto.page() -1, getUrlsRequestDto.size());
        UrlSpecification urlSpecification = new UrlSpecification(getUrlsRequestDto);
        Page<UrlEntity> urlEntityPage = urlRepository.findAll(urlSpecification, pageable);

        if (urlEntityPage.isEmpty()) {
            CustomLogger.logInfo(UrlServiceImpl.class, "No URLs found for user ID: " + userId);
            return new ApiPaginationResponse(
                    0,
                    0,
                    getUrlsRequestDto.page(),
                    getUrlsRequestDto.size(),
                    true,
                    true, false,
                    false,
                    null);
        }

        return new ApiPaginationResponse(
                urlEntityPage.getTotalElements(),
                urlEntityPage.getTotalPages(),
                getUrlsRequestDto.page(),
                getUrlsRequestDto.size(),
                urlEntityPage.isLast(),
                urlEntityPage.isFirst(),
                urlEntityPage.hasNext(),
                urlEntityPage.hasPrevious(),
                urlMapper.toResponseDtoList(urlEntityPage.getContent())
        );
    }

    @Override
    public PatchUrlResponseDto toggleUrlStatus(PatchUrlRequestDto patchUrlRequestDto, Long urlId, Long userId) {
        CustomLogger.logInfo(UrlServiceImpl.class, "Toggling URL status for user ID: " + userId);
        UrlEntity urlEntity = urlRepository.findByIdAndUserIdAndNotDeleted(urlId, userId).orElse(null);

        if (urlEntity == null) {
            throw new UrlException(
                    UrlException.URL_NOT_FOUND_CODE,
                    UrlException.URL_NOT_FOUND_MESSAGE,
                    UrlException.URL_NOT_FOUND_CAUSE
            );
        }

        boolean isActive = patchUrlRequestDto.isActive() != null ? patchUrlRequestDto.isActive() : urlEntity.isActive();

        int result = urlRepository.toggleUrlStatus(urlId, userId, isActive);
        if (result <= 0) {
            throw new UrlException(
                    UrlException.URL_STATUS_UPDATE_FAILED_CODE,
                    UrlException.URL_STATUS_UPDATE_FAILED_MESSAGE,
                    UrlException.URL_STATUS_UPDATE_FAILED_CAUSE
            );
        }

        return urlMapper.toPatchResponseDto(urlEntity);
    }

    @Override
    public PutUrlResponseDto updateUrl(PutUrlRequestDto putUrlRequestDto, Long urlId, Long userId) {
        CustomLogger.logInfo(UrlServiceImpl.class, "Updating URL with ID: " + urlId + " for user ID: " + userId);
        UrlEntity urlEntity = urlRepository.findByIdAndUserIdAndNotDeleted(urlId, userId).orElse(null);

        if (urlEntity == null) {
            throw new UrlException(
                    UrlException.URL_NOT_FOUND_CODE,
                    UrlException.URL_NOT_FOUND_MESSAGE,
                    UrlException.URL_NOT_FOUND_CAUSE
            );
        }

        int result = urlRepository.updateUrl(
                urlId,
                userId,
                putUrlRequestDto.customAlias(),
                putUrlRequestDto.url(),
                putUrlRequestDto.description(),
                putUrlRequestDto.validSince(),
                putUrlRequestDto.validUntil(),
                putUrlRequestDto.isActive()
        );

        if (result <= 0) {
            throw new UrlException(
                    UrlException.URL_UPDATE_FAILED_CODE,
                    UrlException.URL_UPDATE_FAILED_MESSAGE,
                    UrlException.URL_UPDATE_FAILED_CAUSE
            );
        }

        // Update the URL entity with the new values
        urlEntity.setCustomAlias(putUrlRequestDto.customAlias());
        urlEntity.setOriginalUrl(putUrlRequestDto.url());
        urlEntity.setDescription(putUrlRequestDto.description());
        urlEntity.setValidSince(putUrlRequestDto.validSince());
        urlEntity.setValidUntil(putUrlRequestDto.validUntil());
        urlEntity.setActive(putUrlRequestDto.isActive());

        return urlMapper.toPutResponseDto(urlEntity);
    }

    @Override
    public DeleteUrlResponseDto deleteUrl(Long urlId, Long userId) {
        CustomLogger.logInfo(UrlServiceImpl.class, "Deleting URL with ID: " + urlId + " for user ID: " + userId);
        UrlEntity urlEntity = urlRepository.findByIdAndUserIdAndNotDeleted(urlId, userId).orElse(null);

        if (urlEntity == null) {
            throw new UrlException(
                    UrlException.URL_NOT_FOUND_CODE,
                    UrlException.URL_NOT_FOUND_MESSAGE,
                    UrlException.URL_NOT_FOUND_CAUSE
            );
        }

        int result = urlRepository.softDeleteUrl(urlId, userId);

        if (result <= 0) {
            throw new UrlException(
                    UrlException.URL_DELETE_FAILED_CODE,
                    UrlException.URL_DELETE_FAILED_MESSAGE,
                    UrlException.URL_DELETE_FAILED_CAUSE
            );
        }

        return new DeleteUrlResponseDto(true);
    }



}
