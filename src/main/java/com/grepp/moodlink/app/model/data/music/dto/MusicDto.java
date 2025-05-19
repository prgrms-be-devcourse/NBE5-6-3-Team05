package com.grepp.moodlink.app.model.data.music.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.grepp.moodlink.app.model.data.ContentDto;
import com.grepp.moodlink.app.model.data.music.entity.Music;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MusicDto implements ContentDto {

    private String type = "music";

    private String id;
    private String title;
    private String genre;
    private String singer;
    private String description;
    @JsonProperty("release_date")
    private LocalDate releaseDate;
    private String lyrics;
    private String thumbnail;
    private Long likeCount;
    private Boolean activated;
    private String summary;

    public MusicDto(String title, String singer, String thumbnail, LocalDate releaseDate,
        String lyric) {
        this.title = title;
        this.singer = singer;
        this.thumbnail = thumbnail;
        this.releaseDate = releaseDate;
        this.lyrics = lyric;
    }

    public static MusicDto toDto(Music music) {
        MusicDto dto = new MusicDto();
        dto.setId(music.getId());
        dto.setTitle(music.getTitle());
        dto.setGenre(music.getGenre());
        dto.setSinger(music.getSinger());
        dto.setDescription(music.getDescription());
        dto.setReleaseDate(music.getReleaseDate());
        dto.setLyrics(music.getLyrics());
        dto.setThumbnail(music.getThumbnail());
        dto.setLikeCount(music.getLikeCount());

        return dto;
    }

}
