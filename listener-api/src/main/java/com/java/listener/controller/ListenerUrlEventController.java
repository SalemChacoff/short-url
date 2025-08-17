package com.java.listener.controller;

import com.java.listener.dto.request.GetUrlEventRequestDto;
import com.java.listener.dto.response.GetUrlEventResponseDto;
import com.java.listener.usecase.IListenerUrlEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/listener")
@RequiredArgsConstructor
public class ListenerUrlEventController {

    IListenerUrlEventService listenerUrlEventService;

    @PostMapping("/event")
    public GetUrlEventResponseDto handleEvent(@RequestBody GetUrlEventRequestDto getUrlEventRequestDto) {
        // Logic to handle the event
        return listenerUrlEventService.getUrlEventByShortUrl(getUrlEventRequestDto);
    }
}
