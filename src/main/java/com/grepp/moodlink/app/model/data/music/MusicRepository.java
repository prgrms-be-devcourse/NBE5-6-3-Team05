package com.grepp.moodlink.app.model.data.music;

import com.grepp.moodlink.app.model.data.music.entity.Music;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MusicRepository extends JpaRepository<Music, Long> {

}
