package com.java.admin.usecase.url;

import com.java.admin.dto.ApiPaginationResponse;
import com.java.admin.dto.url.request.CreateUrlRequestDto;
import com.java.admin.dto.url.request.GetUrlsRequestDto;
import com.java.admin.dto.url.request.PatchUrlRequestDto;
import com.java.admin.dto.url.request.PutUrlRequestDto;
import com.java.admin.dto.url.response.CreateUrlResponseDto;
import com.java.admin.dto.url.response.DeleteUrlResponseDto;
import com.java.admin.dto.url.response.PatchUrlResponseDto;
import com.java.admin.dto.url.response.PutUrlResponseDto;

public interface IUrlService {

    CreateUrlResponseDto createUrl(CreateUrlRequestDto createUrlRequestDto, Long userId);
    ApiPaginationResponse getAllUrlsByUserId(GetUrlsRequestDto getUrlsRequestDto, Long userId);
    PatchUrlResponseDto toggleUrlStatus(PatchUrlRequestDto patchUrlRequestDto, Long urlId, Long userId);
    PutUrlResponseDto updateUrl(PutUrlRequestDto putUrlRequestDto, Long urlId, Long userId);
    DeleteUrlResponseDto deleteUrl(Long urlId, Long userId);
}
