package com.grepp.moodlink.app.model.admin.movie;

import com.grepp.moodlink.app.model.data.movie.entity.Movie;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminMovieRepository extends JpaRepository<Movie, String>, AdminMovieRepositoryCustom {

    boolean existsByTitleAndReleaseDate(String title, LocalDate releaseDate);

}
