package com.grepp.moodlink.app.model.data.movie;

import com.grepp.moodlink.app.model.data.movie.entity.Genre;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Integer> {

    Optional<Genre> findByName(String name);
}