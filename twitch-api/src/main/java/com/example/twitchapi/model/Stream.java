package com.example.twitchapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Calendar;
import java.util.List;

@NoArgsConstructor
@Data
@Entity(name = "Stream")
@JsonIgnoreProperties(value={ "tag_ids" }, allowGetters=true)
public class Stream {
    @Id
    private String id;
    private String user_id;
    private String user_login;
    private String user_name;
    private String game_id;
    private String game_name;
    private String type;
    private String title;
    private String viewer_count;
    private Calendar started_at;
    private String language;
    private String thumbnail_url;
    private Boolean is_mature;
}
