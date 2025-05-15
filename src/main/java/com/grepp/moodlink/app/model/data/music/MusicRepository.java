package com.grepp.moodlink.app.model.data.music;

import com.grepp.moodlink.app.model.data.music.entity.Music;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;

import java.util.List;
import java.util.Optional;

@Repository
public interface MusicRepository extends JpaRepository<Music, String> {

    List<Music> findByEmbeddingIsNull();

//    @Query("SELECT m FROM Music m WHERE LOWER(REPLACE(m.title, ' ', '')) = LOWER(REPLACE(m.title, ' ', ''))")
//    Optional<Music> findByTitleIgnoreCaseContaining(String trim);

    Optional<Music> findByTitle(String title);
}
