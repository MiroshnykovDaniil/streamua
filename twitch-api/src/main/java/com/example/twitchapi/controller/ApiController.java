package com.example.twitchapi.controller;
import com.example.twitchapi.service.StreamService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/twitch")
@RestController()
public class ApiController {
    private StreamService service;
    public ApiController(StreamService service) {
        this.service = service;
    }
    @GetMapping("/get")
    public String get() throws JsonProcessingException {
        service.getUkrainianStreamList();
        return ResponseEntity.ok().toString();
    }
}