package com.grepp.moodlink.app.model.recomend;

import com.grepp.moodlink.app.model.recomend.entity.LikeDetailSong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeDetailSongRepository extends JpaRepository<LikeDetailSong,String>{
    LikeDetailSong findByLikeId(String likeId);
}
