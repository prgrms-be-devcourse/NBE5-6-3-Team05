package com.grepp.moodlink.app.model.details.dto;

import com.grepp.moodlink.app.model.data.movie.entity.Genre;
import com.grepp.moodlink.app.model.data.movie.entity.Movie;
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
    private LocalDate createdAt;
    private LocalDate modifiedAt;
    private LocalDate releaseDate;

    public MovieDetailsDto(String name, List<String> genre, String description,
        LocalDate createdAt, LocalDate releaseDate, String movieImg) {
        this.name = name;
        this.genre = genre;
        this.description = description;
        this.createdAt = createdAt;
        this.releaseDate = releaseDate;
        this.movieImg = movieImg;
    }

    public static MovieDetailsDto from(Movie movie){
        List<String> genreNames = movie.getGenres()
            .stream()
            .map(Genre::getName)
            .toList();
        return new MovieDetailsDto(
            movie.getTitle(),
            genreNames,
            movie.getDescription(),
            movie.getCreatedAt(),
            movie.getReleaseDate(),
            movie.getThumbnail()
        );

    }

}
