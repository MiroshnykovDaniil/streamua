package com.example.twitchapi.service;
import com.example.twitchapi.model.Channel;
import com.example.twitchapi.model.Stream;
import com.example.twitchapi.repository.ChannelRepository;
import com.example.twitchapi.repository.StreamRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.jayway.jsonpath.JsonPath;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.logging.Logger;

@Service
@Transactional
public class StreamService {
    private final StreamRepository streamRepository;
    private final ChannelRepository channelRepository;
    public final Logger log = Logger.getLogger("StreamService");


    public StreamService(StreamRepository streamRepository, ChannelRepository channelRepository) {
        this.streamRepository = streamRepository;
        this.channelRepository = channelRepository;
    }

    @PostConstruct
    public void init(){
        Thread t = new Thread(){
            @Override
            public void run() {
                while(true){
                    try{
                        // we execute update thread every 5 minutes
                        getUkrainianStreamList();
                        log.info("StreamService.init: thread sleeps");
                        Thread.sleep(1000*60*1);
                        log.info("StreamService.init: thread woke up");
                    }
                    catch (InterruptedException | JsonProcessingException e){
                    }
                }
            }
        };
        t.start();
    }

    public boolean getUkrainianStreamList() throws JsonProcessingException {
        // we need to delete old streams that are not live now
        // @todo to think about better mechanism to deal with outdated info
        streamRepository.deleteAllInBatch();
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
        // Twitch Api gives only 20 records, cursors is used for pagination
        // @todo write better parser
        String cursor = response.substring(response.indexOf(("\"cursor\":"))+10,response.indexOf(("\"}}")));
        // So we gonna find all the streams!
        Set<Stream> completedStreams = completeUkrainianStreamList((new HashSet<>(streams)),cursor);

        List<Channel> channels;
        channelRepository.setAllChannelsOffline();
        List<Channel> finalChannels=new ArrayList<>();
        completedStreams.forEach(stream -> {
            Channel channel = new Channel(
                    stream.getUser_id(),
                    stream.getUser_login(),
                    stream.getLanguage(),
                    stream.getUser_name(),
                    stream.getGame_id(),
                    stream.getGame_name(),
                    stream.getType(),
                    stream.getTitle(),
                    stream.getStarted_at()
            );
            finalChannels.add(channel);
        });
        streamRepository.saveAll(completedStreams);
        channelRepository.saveAll(finalChannels);
        addChannelsLogo();
        return true;
    }

    public void addChannelsLogo(){
        List<Channel> channels = channelRepository.findAll();
        List<Channel> updated = new ArrayList<>();
        channels.forEach(channel ->{
            if(channel.getThumbnail_url()==null){
                channel.setThumbnail_url(
                        getChannel(channel.getDisplay_name()).getThumbnail_url()
                );
                updated.add(channel);
            }
        });
        channelRepository.saveAll(updated);
    }

    // This we will once or two a week to update outdated logos
    public void updateAllChannelLogo(){
        List<Channel> channels = channelRepository.findAll();
        channels.forEach(channel ->{
                channel.setThumbnail_url(
                        getChannel(channel.getDisplay_name()).getThumbnail_url()
                );
        });
        channelRepository.saveAll(channels);
    }

    // get info about channel from Twitch API
    public Channel getChannel(String channelName){
        WebClient webClient = WebClient
                .builder()
                .baseUrl("https://api.twitch.tv/helix")
                .defaultHeader(HttpHeaders.AUTHORIZATION,"Bearer 6zwfw0sl7zfua6jpswoiezfngchcg6")
                .build();
        String response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search/channels")
                            .queryParam("query",channelName)
                            .queryParam("first","1")
                        .build())
                .header("Client-id","4gzf6imth4qwaomfugkd202f721x90")
                .retrieve()
                .bodyToMono(String.class)
                .block();
        List<Map> nodes = JsonPath.parse(response).read("$..data.*");
        ObjectMapper mapper = new ObjectMapper();
        CollectionType usersType = mapper.getTypeFactory().constructCollectionType(Set.class, Channel.class);
        Set<Channel> retrievedChannel = mapper.convertValue(nodes, usersType);
        return retrievedChannel.iterator().next();
    }

    // completing list of all ukrainian streams because of twitch api pagination
    public Set<Stream> completeUkrainianStreamList(Set<Stream> streams, String cursor){
        boolean exit = false;
        WebClient webClient = WebClient
                .builder()
                .baseUrl("https://api.twitch.tv/helix")
                .defaultHeader(HttpHeaders.AUTHORIZATION,"Bearer 6zwfw0sl7zfua6jpswoiezfngchcg6")
                .build();
        String response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/streams")
                        .queryParam("after",cursor)
                        .queryParam("language","uk")
                        .build())
                .header("Client-id","4gzf6imth4qwaomfugkd202f721x90")
                .retrieve()
                .bodyToMono(String.class)
                .block();
        if(response.length()<30) return streams;
        {
            List<Map> nodes = JsonPath.parse(response).read("$..data.*");
            ObjectMapper mapper = new ObjectMapper();
            CollectionType usersType = mapper.getTypeFactory().constructCollectionType(Set.class, Stream.class);
            Set<Stream> responseStreams = mapper.convertValue(nodes, usersType);

            log.info("completeUkrainianStreamList: response: "+response);

            String responseCursor = response.substring(response.indexOf(("\"cursor\":")) + 10, response.indexOf(("\"}}")));
            streams.addAll(responseStreams);
            streams.addAll(completeUkrainianStreamList(streams,responseCursor));
        }
        return streams;
    }
}
