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
//        WebClient webClient = WebClient
//                .builder()
//                .baseUrl("https://api.twitch.tv/helix")
//                .defaultHeader(HttpHeaders.AUTHORIZATION,"Bearer 6zwfw0sl7zfua6jpswoiezfngchcg6")
//                .build();
//
//        String response = webClient.get()
//                .uri(uriBuilder -> uriBuilder
//                        .path("/streams")
//                        .queryParam("language","uk")
//                        .build())
//                .header("Client-id","4gzf6imth4qwaomfugkd202f721x90")
//                .retrieve()
//                .bodyToMono(String.class)
//                .block();
        return ResponseEntity.ok().toString();
    }
}