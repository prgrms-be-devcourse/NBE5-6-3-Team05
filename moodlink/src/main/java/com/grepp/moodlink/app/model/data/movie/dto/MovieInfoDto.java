package com.grepp.moodlink.app.model.data.movie.dto;


import com.grepp.moodlink.app.model.data.movie.entity.Genre;
import com.grepp.moodlink.app.model.data.movie.entity.Movie;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovieInfoDto {

    private String id;
    private String title;

    private Set<Genre> genres = new HashSet<>();
    private String description;

    private float[] embedding;
    private LocalDate releaseDate;
    private LocalDate createdAt;
    private String thumbnail;
    private Long likeCount = 0L;

    public static MovieInfoDto toDto(Movie movie) {
        MovieInfoDto dto = new MovieInfoDto();
        dto.setId(movie.getId());
        dto.setTitle(movie.getTitle());
        Set<Genre> genres = new HashSet<>();
        for (Genre genre : movie.getGenres()) {
            genres.add(new Genre(genre.getId(), genre.getName()));
        }
        dto.setGenres(genres);
        dto.setGenres(movie.getGenres());
        dto.setDescription(movie.getDescription());
        dto.setReleaseDate(movie.getReleaseDate());
        dto.setCreatedAt(movie.getCreatedAt());
        dto.setThumbnail(movie.getThumbnail());
        dto.setLikeCount(movie.getLikeCount());

        return dto;


    }

}
