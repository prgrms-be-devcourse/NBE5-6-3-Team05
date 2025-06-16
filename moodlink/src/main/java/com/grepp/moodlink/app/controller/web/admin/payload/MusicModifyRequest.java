package com.grepp.moodlink.app.controller.web.admin.payload;

import com.grepp.moodlink.app.model.data.music.dto.MusicDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

@Data
public class MusicModifyRequest {

    private List<MultipartFile> thumbnail;
    @NotNull
    private String genre;

    private String description;
    @NotBlank
    private String lyrics;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;

    public MusicDto toDto() {
        MusicDto dto = new MusicDto();
        dto.setGenre(genre);
        dto.setDescription(description);
        dto.setLyrics(lyrics);
        dto.setReleaseDate(releaseDate);
        return dto;
    }

}
