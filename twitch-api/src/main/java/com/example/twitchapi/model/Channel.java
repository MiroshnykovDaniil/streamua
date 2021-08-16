package com.example.twitchapi.model;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.Instant;
import java.util.Calendar;

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
    private Calendar started_at;


    public Channel(
            String id,
            String broadcaster_login,
            String broadcaster_language,
            String display_name,
            String game_id,
            String game_name,
            String is_live,
            String title,
            Calendar started_at) {
        this.id=id;
        this.broadcaster_language=broadcaster_language;
        this.broadcaster_login=broadcaster_login;
        this.display_name=display_name;
        this.game_id=game_id;
        this.game_name=game_name;
        this.is_live=is_live;
        this.title=title;
        this.started_at=started_at;
    }

}
