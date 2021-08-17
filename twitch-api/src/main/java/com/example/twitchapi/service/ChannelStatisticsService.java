package com.example.twitchapi.service;


import com.example.twitchapi.model.Channel;
import com.example.twitchapi.model.ChannelStatistics;
import com.example.twitchapi.model.Stream;
import com.example.twitchapi.repository.ChannelRepository;
import com.example.twitchapi.repository.ChannelStatisticsRepository;
import com.example.twitchapi.repository.StreamRepository;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;

@Service
public class ChannelStatisticsService {
    public final Logger log = Logger.getLogger("ChannelStatisticsService");
    private final ChannelRepository channelRepository;
    private final ChannelStatisticsRepository channelStatisticsRepository;
    private final StreamRepository streamRepository;

    public ChannelStatisticsService(ChannelRepository channelRepository, ChannelStatisticsRepository channelStatisticsRepository, StreamRepository streamRepository) {
        this.channelRepository = channelRepository;
        this.channelStatisticsRepository = channelStatisticsRepository;
        this.streamRepository = streamRepository;
    }

    @PostConstruct
    public void getStatistics(){

        Thread t = new Thread(() -> {
            while(true){
                try {
                    List<Channel> channels = channelRepository.findAll();
                    List<Stream> streams = streamRepository.findAll();
                    List<ChannelStatistics> channelStatisticsList = new ArrayList<>();
                    channels.forEach(channel -> {
                                ChannelStatistics channelStatistics = new ChannelStatistics();
                                channelStatistics.setChannelId(channel);
                                channelStatistics.setStatsTime(Calendar.getInstance());
                                channelStatistics.setIs_live(channel.getIs_live());
                        channelStatisticsList.add(channelStatistics);
                    });
                    channelStatisticsRepository.saveAll(channelStatisticsList);
                    log.info("StreamService.init: thread sleeps");
                    Thread.sleep(1000*60*3);
                    log.info("StreamService.init: thread woke up");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }
}
