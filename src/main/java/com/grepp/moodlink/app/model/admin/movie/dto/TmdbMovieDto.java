package com.grepp.moodlink.app.model.admin.movie.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.grepp.moodlink.app.model.data.ContentDto;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TmdbMovieDto {

    private String type = "movie";

    private boolean adult;
    private String backdropPath;
    @JsonProperty("genre_ids")
    private List<Integer> genreIds;
    private int id;
    @JsonProperty("original_language")
    private String originalLanguage;
    @JsonProperty("original_title")
    private String originalTitle;
    private String overview;
    private double popularity;
    @JsonProperty("poster_path")
    private String posterPath;
    @JsonProperty("release_date")
    private LocalDate releaseDate;
    private String title;
    private boolean video;
    @JsonProperty("vote_average")
    private double voteAverage;
    @JsonProperty("vote_count")
    private int voteCount;
}

