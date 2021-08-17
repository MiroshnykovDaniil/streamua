package com.example.twitchapi.repository;

import com.example.twitchapi.model.ChannelStatistic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChannelStatisticsRepository extends JpaRepository<ChannelStatistic,String> {


}
