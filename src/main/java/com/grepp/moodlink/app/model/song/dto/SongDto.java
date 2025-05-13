package com.grepp.moodlink.app.model.song.dto;


import com.grepp.moodlink.app.model.song.entity.Song;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class SongDto {


    private String id;

    private String title;
    private String genre;
    private String singer;
    private String description;
    private LocalDate releaseDate;
    private LocalDateTime createdAt;
    private String thumbnail;
    private Long likeCount;

    public static SongDto toDto(Song song) {
        SongDto dto = new SongDto();
        dto.setId(song.getId());
        dto.setTitle(song.getTitle());
        dto.setGenre(song.getGenre());
        dto.setSinger(song.getSinger());
        dto.setDescription(song.getDescription());
        dto.setReleaseDate(song.getReleaseDate());
        dto.setCreatedAt(song.getCreatedAt());
        dto.setThumbnail(song.getThumbnail());
        dto.setLikeCount(song.getLikeCount());

        return dto;
    }

}
