package com.grepp.moodlink.app.model.details.dto;

import com.grepp.moodlink.app.model.data.music.dto.MusicDto;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SongDetailsDto {

    private String id;
    private String name;
    private String artist;
    private LocalDate releasedAt;
    private String genre;
    private String description;
    private String songImg;
    private String externalLink;
    private String lyrics;
    private String summary;
    private Boolean status;

    public SongDetailsDto(String id, String name, String artist, LocalDate releasedAt,
        String genre, String description, String songImg, String lyrics, String summary) {
        this.id = id;
        this.name = name;
        this.artist = artist;
        this.releasedAt = releasedAt;
        this.genre = genre;
        this.description = description;
        this.songImg = songImg;
        this.lyrics = lyrics;
        this.summary = summary;
        this.status = false;
        this.externalLink = "https://www.google.com/search?q=" + name;
    }

    public static SongDetailsDto from(MusicDto music) {
        return new SongDetailsDto(
            music.getId(),
            music.getTitle(),
            music.getSinger(),
            music.getReleaseDate(),
            music.getGenre(),
            music.getDescription(),
            music.getThumbnail(),
            music.getLyrics(),
            music.getSummary()
        );
    }
}
