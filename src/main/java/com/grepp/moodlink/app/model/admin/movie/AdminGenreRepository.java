package com.grepp.moodlink.app.model.admin.movie;

import com.grepp.moodlink.app.model.data.movie.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminGenreRepository extends JpaRepository<Genre, Integer> {

}