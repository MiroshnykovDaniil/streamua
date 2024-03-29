package com.example.twitchapi.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Calendar;

@Data
@NoArgsConstructor
@Entity
public class ChannelStatistic {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    private Calendar statsTime;

    @ManyToOne
    @JoinColumn(name = "channel_id",referencedColumnName = "id")
    private Channel channelId;

    private String is_live;

    private String viewerCount;

}
