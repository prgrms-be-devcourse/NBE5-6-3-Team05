package com.grepp.moodlink.app.model.recomend;

import com.grepp.moodlink.app.model.recomend.entity.LikeDetailMusic;
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
public interface LikeDetailMusicRepository extends JpaRepository<LikeDetailMusic, Long> {



    List<LikeDetailMusic> findAllByLikesIdIn(Collection<Long> likesIds);
    @Query("SELECT l FROM LikeDetailMusic l WHERE l.likesId IN :likesIds")
    Page<LikeDetailMusic> findAllByLikesIdInPagination(@Param("likesIds") Collection<Long> likesIds, Pageable pageable);

    List<LikeDetailMusic> findByLikesId(Long likeId);

    @Modifying
    @Transactional
    @Query("DELETE FROM LikeDetailMusic l WHERE l.musicId = :musicId AND l.likesId = :likesId")
    void deleteByMusicIdAndLikesId(@Param("musicId")String musicId,@Param("likesId") Long likesId);
}
