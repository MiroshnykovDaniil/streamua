package com.example.twitchapi.repository;


import com.example.twitchapi.model.Channel;
import com.example.twitchapi.model.Stream;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface ChannelRepository extends JpaRepository<Channel,String> {
        @Modifying
        @Transactional
        @Query(value="update channel ch set ch.is_live = 'false'", nativeQuery = true)
        void setAllChannelsOffline();

}
