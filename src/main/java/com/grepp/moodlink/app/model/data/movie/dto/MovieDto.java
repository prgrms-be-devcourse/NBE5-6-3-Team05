package com.grepp.moodlink.app.model.data.movie.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovieDto {

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
    private String releaseDate;
    private String title;
    private boolean video;
    private double voteAverage;
    private int voteCount;
    @JsonProperty("genre_names")
    private List<String> genreNames;
    private Boolean activated;
}