package com.grepp.moodlink.app.model.data.movie.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.grepp.moodlink.app.model.data.ContentDto;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovieDto implements ContentDto {
    private String type = "movie";

    private boolean adult;
    private String backdropPath;
    @JsonProperty("genre_ids")
    private List<Integer> genreIds;
    private int id;
    private String originalLanguage;
    private String originalTitle;
    private String overview;
    private double popularity;
    @JsonProperty("poster_path")
    private String posterPath;
    @JsonProperty("release_date")
    private LocalDate releaseDate;
    private String title;
    private boolean video;
    private double voteAverage;
    private int voteCount;
    @JsonProperty("genre_names")
    private List<String> genreNames;
    private Boolean activated;

    public MovieDto(String title, String overview, LocalDate releaseDate, String backdropPath) {
        this.title = title;
//        this.genreNames = genreNames;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.backdropPath = backdropPath;
    }
}