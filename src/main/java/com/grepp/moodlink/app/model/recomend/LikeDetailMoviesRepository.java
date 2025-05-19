package com.grepp.moodlink.app.model.recomend;

import com.grepp.moodlink.app.model.recomend.entity.LikeDetailMovies;
import feign.Param;
import jakarta.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeDetailMoviesRepository extends JpaRepository<LikeDetailMovies, Long> {


    List<LikeDetailMovies> findAllByLikesIdIn(Collection<Long> likesIds);
    @Query("SELECT l FROM LikeDetailMovies l WHERE l.likesId IN :likesIds")
    Page<LikeDetailMovies> findAllByLikesIdInPagination(@Param("likesIds") Collection<Long> likesIds, Pageable pageable);

    List<LikeDetailMovies> findByLikesId(Long likeId);

    @Modifying
    @Transactional
    @Query("DELETE FROM LikeDetailMovies l WHERE l.movieId = :movieId AND l.likesId = :likesId")
    void deleteByMovieIdAndLikesId(@Param("movieId")String movieId,@Param("likesId") Long likesId);
}
