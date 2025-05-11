package com.grepp.moodlink.app.model.recomend;

import com.grepp.moodlink.app.model.movie.entity.Movie;
import com.grepp.moodlink.app.model.recomend.entity.LikeDetailMovies;
import com.grepp.moodlink.app.model.recomend.entity.LikeDetailsong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeDetailMoviesRepository extends JpaRepository<LikeDetailMovies,String> {
}
