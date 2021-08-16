package com.example.twitchapi.model;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.Instant;

@NoArgsConstructor
@Data
@Entity(name = "Channel")
public class Channel {

    @Id
    private String id;
    private String broadcaster_login;
    private String broadcaster_language;
    private String display_name;
    private String game_id;
    private String game_name;
    private String is_live;
    private String thumbnail_url;
    private String title;
    private Instant started_at;


}
