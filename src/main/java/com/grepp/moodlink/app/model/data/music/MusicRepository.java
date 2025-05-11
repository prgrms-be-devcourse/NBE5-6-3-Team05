package com.grepp.moodlink.app.model.data.music;

import com.grepp.moodlink.app.model.data.music.entity.Music;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MusicRepository extends JpaRepository<Music, Long> {

    List<Music> findByEmbeddingIsNull();
}
