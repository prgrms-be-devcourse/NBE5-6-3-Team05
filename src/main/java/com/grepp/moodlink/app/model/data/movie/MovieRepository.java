package com.grepp.moodlink.app.model.data.movie;

import com.grepp.moodlink.app.model.data.movie.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, String> {

    List<Movie> findByEmbeddingIsNull();

}
