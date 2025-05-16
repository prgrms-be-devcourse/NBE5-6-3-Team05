package com.grepp.moodlink.app.model.recomend;

import com.grepp.moodlink.app.model.recomend.entity.LikeDetailMovies;
import feign.Param;
import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeDetailMoviesRepository extends JpaRepository<LikeDetailMovies, Long> {


    List<LikeDetailMovies> findAllByLikesIdIn(Collection<String> likesIds);
    @Query("SELECT l FROM LikeDetailMovies l WHERE l.likesId IN :likesIds")
    Page<LikeDetailMovies> findAllByLikesIdInPagination(@Param("likesIds") Collection<String> likesIds, Pageable pageable);

    LikeDetailMovies findByLikesId(Long likeId);

    void deleteByMovieId(String id);
}
