package com.example.twitchapi.service;


import com.example.twitchapi.model.Channel;
import com.example.twitchapi.model.ChannelStatistic;
import com.example.twitchapi.model.Stream;
import com.example.twitchapi.repository.ChannelRepository;
import com.example.twitchapi.repository.ChannelStatisticsRepository;
import com.example.twitchapi.repository.StreamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
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
    public void init(){
        Thread t = new Thread(() -> {
            while(true){
                try {
                    getStatistics();
                    log.info("StreamService.init: thread sleeps");
                    Thread.sleep(1000*60*1);
                    log.info("StreamService.init: thread woke up");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }

    @Transactional
    public void getStatistics(){
        List<Stream> streams = streamRepository.findAll();
        List<ChannelStatistic> channelStatisticsList = new ArrayList<>();
        streams.forEach(stream -> {
            Channel ch = new Channel();
            ch.setId(stream.getUser_id());
            ChannelStatistic channelStatistics = new ChannelStatistic();
            channelStatistics.setChannelId(ch);
            channelStatistics.setViewerCount(stream.getViewer_count());
            channelStatistics.setIs_live(stream.getType());
            channelStatistics.setStatsTime(Calendar.getInstance());
            channelStatisticsList.add(channelStatistics);
        });
        channelStatisticsRepository.saveAll(channelStatisticsList);

    }

}
