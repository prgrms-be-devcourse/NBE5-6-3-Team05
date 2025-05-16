package com.grepp.moodlink.app.controller.web.admin.payload;

import com.grepp.moodlink.app.model.data.movie.dto.MovieInfoDto;
import com.grepp.moodlink.app.model.data.movie.entity.Genre;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class MovieAddRequest {

    private List<MultipartFile> thumbnail;
    @NotBlank
    private String title;
    @NotBlank
    private String genres;
    private String description;
    @NotNull
    private LocalDate releaseDate;

    public MovieInfoDto toDto(){
        MovieInfoDto movieInfoDto = new MovieInfoDto();
        movieInfoDto.setTitle(title);
        movieInfoDto.setDescription(description);
        movieInfoDto.setReleaseDate(releaseDate);
        Set<Genre> genres = new HashSet<>();

        //genres를 ,를 기준으로 잘라서 만들기
        String[] splitGenres = this.genres.split(",");

        for (String genre : splitGenres) {
            // : 를 기준으로 id와 name을 자르기
            String[] splitIdName = genre.split(":");
            genres.add(new Genre(Integer.parseInt(splitIdName[0]),splitIdName[1]));
        }

        movieInfoDto.setGenres(genres);

        return movieInfoDto;
    }
}
