package com.example.twitchapi.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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

//    @ManyToOne
//    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
//    @JoinColumn(name = "user_id",referencedColumnName = "id")
//    @JoinColumn(name = "game_name",referencedColumnName = "game_name")
//    @JoinColumn(name = "game_id",referencedColumnName = "game_id")
//    @JoinColumn(name = "user_login",referencedColumnName = "broadcaster_login")
//    @JoinColumn(name = "user_name",referencedColumnName = "display_name")
//    @JoinColumn(name = "type",referencedColumnName = "is_live")
//    @JoinColumn(name = "title",referencedColumnName = "title")
//    @JoinColumn(name = "started_at",referencedColumnName = "started_at")
//    @JoinColumn(name = "language",referencedColumnName = "broadcaster_language")
//    private Channel channel;
}
