package com.java.listener.usecase;

import com.java.listener.dto.request.GetUrlEventRequestDto;
import com.java.listener.dto.response.GetUrlEventResponseDto;

public interface IListenerUrlEventService {

    GetUrlEventResponseDto getUrlEventByShortUrl(GetUrlEventRequestDto getUrlEventRequestDto);
}
