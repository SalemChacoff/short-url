package com.java.admin.usecase.url;

import com.java.admin.dto.ApiPaginationResponse;
import com.java.admin.dto.url.request.CreateUrlRequestDto;
import com.java.admin.dto.url.request.GetUrlsRequestDto;
import com.java.admin.dto.url.response.CreateUrlResponseDto;

public interface IUrlService {

    CreateUrlResponseDto createUrl(CreateUrlRequestDto createUrlRequestDto, Long userId);
    ApiPaginationResponse getAllUrlsByUserId(GetUrlsRequestDto getUrlsRequestDto, Long userId);
}
