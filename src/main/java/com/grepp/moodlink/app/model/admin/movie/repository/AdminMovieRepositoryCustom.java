package com.grepp.moodlink.app.model.admin.movie.repository;

import com.grepp.moodlink.app.model.data.movie.entity.Movie;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminMovieRepositoryCustom {

    Page<Movie> findPaged(Pageable pageable);

    Optional<Movie> findByIdWithGenre(String id);

    Long countMoviesByGenre(Integer id);
}
