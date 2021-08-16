package com.example.twitchapi.service;

import com.example.twitchapi.model.Stream;
import com.example.twitchapi.repository.StreamRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.jayway.jsonpath.JsonPath;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
public class StreamService {

    private final StreamRepository streamRepository;

    public StreamService(StreamRepository streamRepository) {
        this.streamRepository = streamRepository;
    }

    public boolean getUkrainianStreamList() throws JsonProcessingException {

        // we need to delete old streams that are not live now
        // @todo to think about better mechanism to deal with outdated info
        streamRepository.deleteAll();

        WebClient webClient = WebClient
                .builder()
                .baseUrl("https://api.twitch.tv/helix")
                .defaultHeader(HttpHeaders.AUTHORIZATION,"Bearer 6zwfw0sl7zfua6jpswoiezfngchcg6")
                .build();
        String response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/streams")
                        .queryParam("language","uk")
                        .build())
                .header("Client-id","4gzf6imth4qwaomfugkd202f721x90")
                .retrieve()
                .bodyToMono(String.class)
                .block();

        List<Map> nodes = JsonPath.parse(response).read("$..data.*");
        ObjectMapper mapper = new ObjectMapper();
        CollectionType usersType = mapper.getTypeFactory().constructCollectionType(List.class, Stream.class);
        List<Stream> streams = mapper.convertValue(nodes, usersType);
        streamRepository.saveAll(streams);
        return true;
    }

}
