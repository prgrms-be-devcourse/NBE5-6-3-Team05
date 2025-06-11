package com.grepp.moodlink.app.controller.web.admin.payload;

import com.grepp.moodlink.app.model.data.movie.dto.MovieInfoDto;
import com.grepp.moodlink.app.model.data.movie.entity.Genre;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class MovieAddRequest {
    // 직접 관리자가 컨텐츠를 추가하는 경우 파일로 썸네일을 받음
    private List<MultipartFile> thumbnail;
    // api를 통해 정보를 받아와 추가하는 경우 url 경로로 썸네일을 받음
    private String thumbnailPath;
    @NotBlank(message = "제목을 입력해주세요")
    private String title;
    @NotBlank(message = "장르를 선택해주세요")
    private String genres;
    @NotBlank
    private String description;
    @NotNull(message = "개봉일을 입력해주세요")
    private LocalDate releaseDate;

    public MovieInfoDto toDto() {
        MovieInfoDto movieInfoDto = new MovieInfoDto();
        movieInfoDto.setTitle(title);
        movieInfoDto.setDescription(description);
        movieInfoDto.setReleaseDate(releaseDate);
        movieInfoDto.setThumbnail(thumbnailPath);
        Set<Genre> genres = new HashSet<>();

        //genres를 ,를 기준으로 잘라서 만들기
        String[] splitGenres = this.genres.split(",");

        for (String genre : splitGenres) {
            // : 를 기준으로 id와 name을 자르기
            String[] splitIdName = genre.split(":");
            genres.add(new Genre(Integer.parseInt(splitIdName[0]), splitIdName[1]));
        }

        movieInfoDto.setGenres(genres);

        return movieInfoDto;
    }
}
