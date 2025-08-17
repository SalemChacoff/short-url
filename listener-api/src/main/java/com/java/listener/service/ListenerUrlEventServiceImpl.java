package com.java.listener.service;

import com.java.listener.dto.request.GetUrlEventRequestDto;
import com.java.listener.dto.response.GetUrlEventResponseDto;
import com.java.listener.entity.ListenerUrlEventEntity;
import com.java.listener.repository.ListenerUrlEventRepository;
import com.java.listener.usecase.IListenerUrlEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ListenerUrlEventServiceImpl implements IListenerUrlEventService {


    ListenerUrlEventRepository listenerUrlEventRepository;

    @Override
    public GetUrlEventResponseDto getUrlEventByShortUrl(GetUrlEventRequestDto getUrlEventRequestDto) {

        ListenerUrlEventEntity fullUrl = listenerUrlEventRepository.findListenerUrlEventEntityByShortUrl(getUrlEventRequestDto.shortUrl()).orElse(null);

        if (fullUrl == null) {
            return new GetUrlEventResponseDto(false,"URL not found");
        }

        return new GetUrlEventResponseDto(true, fullUrl.getOriginalUrl());
    }
}
