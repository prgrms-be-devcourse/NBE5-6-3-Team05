package com.grepp.moodlink.app.model.admin.movie.repository;

import com.grepp.moodlink.app.model.data.movie.entity.Genre;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminGenreRepository extends JpaRepository<Genre, Integer> {

    Genre findByName(String name);

    List<Genre> findAllByActivated(Boolean activated);

    Integer id(Integer id);
}