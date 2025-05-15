package com.grepp.moodlink.app.controller.web.admin.payload;

import com.grepp.moodlink.app.model.data.music.dto.MusicDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class MusicAddRequest {

    private List<MultipartFile> thumbnail;
    @NotBlank
    private String title;
    @NotBlank
    private String singer;
    @NotNull
    private String genre;

    private String description;
    private String lyrics;

    private LocalDate releaseDate;

    public MusicDto toDto(){
        MusicDto dto = new MusicDto();
        dto.setTitle(title);
        dto.setGenre(genre);
        dto.setSinger(singer);
        dto.setDescription(description);
        dto.setLyrics(lyrics);
        dto.setReleaseDate(releaseDate);
        return dto;
    }

}
