package com.java.listener.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/listener")
@RequiredArgsConstructor
public class ListenerUrlEventController {


    @PostMapping("/event")
    public String handleEvent() {
        // Logic to handle the event
        return "Event handled successfully";
    }
}
