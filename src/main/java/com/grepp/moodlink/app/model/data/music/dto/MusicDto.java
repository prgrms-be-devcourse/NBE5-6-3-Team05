package com.grepp.moodlink.app.model.data.music.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MusicDto {

    private Integer id;
    private String title;
    private String genre;
    private String singer;
    private String description;
    @JsonProperty("release_date")
    private LocalDate releaseDate;
    private String lyrics;
    private String thumbnail;
    private Long likeCount;

}
