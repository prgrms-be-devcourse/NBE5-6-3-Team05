package com.grepp.moodlink.app.model.details.dto;

import com.grepp.moodlink.app.model.data.movie.dto.MovieInfoDto;
import com.grepp.moodlink.app.model.data.movie.entity.Genre;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MovieDetailsDto {

    private String id;
    private String name;
    private List<String> genre;
    private String description;
    private String movieImg;
    private String externalLink;
    private Boolean status;
    private LocalDate modifiedAt;
    private LocalDate releaseDate;

    public MovieDetailsDto(String id, String name, List<String> genre,
        String description, LocalDate releaseDate, String movieImg) {
        this.id = id;
        this.name = name;
        this.genre = genre;
        this.description = description;
        this.releaseDate = releaseDate;
        this.movieImg = movieImg;
        this.externalLink = "https://www.google.com/search?q=" + name;
        this.status = false;
    }

    public static MovieDetailsDto from(MovieInfoDto movieInfoDto) {
        List<String> genreNames = movieInfoDto.getGenres()
            .stream()
            .map(Genre::getName)
            .toList();
        return new MovieDetailsDto(
            movieInfoDto.getId(),
            movieInfoDto.getTitle(),
            genreNames,
            movieInfoDto.getDescription(),
            movieInfoDto.getReleaseDate(),
            movieInfoDto.getThumbnail()
        );

    }

}
