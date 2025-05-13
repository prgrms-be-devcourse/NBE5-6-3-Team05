package com.grepp.moodlink.app.model.movie.dto;


import com.grepp.moodlink.app.model.movie.entity.Genre;

import com.grepp.moodlink.app.model.movie.entity.Movie;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;

@Data
public class MovieDto {


    private String id;
    private String title;

    private Set<Genre> genres = new HashSet<>();
    private String description;

    private float[] embedding;
    private LocalDate releaseDate;
    private LocalDateTime createdAt;
    private String thumbnail;
    private Long likeCount;

    public static MovieDto toDto(Movie movie) {
        MovieDto dto = new MovieDto();
        dto.setId(movie.getId());
        dto.setTitle(movie.getTitle());
        dto.setGenres(movie.getGenres());
        dto.setDescription(movie.getDescription());
        dto.setReleaseDate(movie.getReleaseDate());
        dto.setCreatedAt(movie.getCreatedAt());
        dto.setThumbnail(movie.getThumbnail());
        dto.setLikeCount(movie.getLikeCount());

        return dto;


    }
}
