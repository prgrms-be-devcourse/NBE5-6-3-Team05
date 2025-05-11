package com.grepp.moodlink.app.model.movie;

import com.grepp.moodlink.app.model.movie.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    List<Movie> findByEmbeddingIsNull();
}
