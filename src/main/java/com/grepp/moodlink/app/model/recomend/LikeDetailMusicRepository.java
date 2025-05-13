package com.grepp.moodlink.app.model.recomend;

import com.grepp.moodlink.app.model.recomend.entity.LikeDetailMusic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeDetailMusicRepository extends JpaRepository<LikeDetailMusic,String>{
    LikeDetailMusic findByLikesId(String likeId);
}
