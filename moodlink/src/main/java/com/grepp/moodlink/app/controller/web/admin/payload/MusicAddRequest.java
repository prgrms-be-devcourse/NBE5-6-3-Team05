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
    // 직접 관리자가 컨텐츠를 추가하는 경우 파일로 썸네일을 받음
    private List<MultipartFile> thumbnail;
    // api를 통해 정보를 받아와 추가하는 경우 url 경로로 썸네일을 받음
    private String thumbnailPath;

    @NotBlank(message = "제목을 입력해주세요")
    private String title;
    @NotBlank(message = "가수를 입력해주세요")
    private String singer;
    @NotNull(message = "장르를 선택해주세요")
    private String genre;

    private String description;
    @NotBlank
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
        dto.setThumbnail(thumbnailPath);
        return dto;
    }

}
