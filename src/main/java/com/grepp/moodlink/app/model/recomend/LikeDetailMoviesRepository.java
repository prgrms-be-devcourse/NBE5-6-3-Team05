package com.grepp.moodlink.app.model.recomend;

import com.grepp.moodlink.app.model.recomend.entity.LikeDetailMovies;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeDetailMoviesRepository extends JpaRepository<LikeDetailMovies,String> {
    LikeDetailMovies findByLikesId(String likeId);
}
