package com.grepp.moodlink.app.model.data.movie;

import com.grepp.moodlink.app.model.data.movie.entity.Movie;
import com.grepp.moodlink.app.model.data.music.entity.Music;
import java.time.LocalDate;
import com.grepp.moodlink.app.model.result.dto.MovieDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, String>, MovieRepositoryCustom {

    List<Movie> findByEmbeddingIsNull();

    @Query("SELECT DISTINCT m FROM Movie m " +
            "JOIN FETCH m.genres g " +
            "WHERE g.name = :genreName")
    List<Movie> findByGenreName(@Param("genreName") String genreName);

    boolean existsByTitleAndReleaseDate(String title, LocalDate releaseDate);

    Optional<Movie> findByTitleContainingIgnoreCase(String title);

    Movie findByTitle(String title);

    List<Movie> findAllByIdIn(List<String> id);

    @Query("SELECT new com.grepp.moodlink.app.model.result.dto.MovieDto(m.id, m.title, m.thumbnail) " +
        "FROM Movie m WHERE m.id = :id")
    Optional<MovieDto> findSimpleById(@Param("id") String id);
}
