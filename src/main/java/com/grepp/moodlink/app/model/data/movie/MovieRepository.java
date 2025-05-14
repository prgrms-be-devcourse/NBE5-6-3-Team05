package com.grepp.moodlink.app.model.data.movie;

import com.grepp.moodlink.app.model.data.movie.entity.Movie;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, String>, MovieRepositoryCustom {

    List<Movie> findByEmbeddingIsNull();

    @Query("SELECT DISTINCT m FROM Movie m " +
            "JOIN FETCH m.genres g " +
            "WHERE g.name = :genreName")
    List<Movie> findByGenreName(@Param("genreName") String genreName);

    boolean existsByTitleAndReleaseDate(String title, LocalDate releaseDate);
}
