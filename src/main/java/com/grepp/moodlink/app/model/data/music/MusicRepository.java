package com.grepp.moodlink.app.model.data.music;

import com.grepp.moodlink.app.model.data.music.entity.Music;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MusicRepository extends JpaRepository<Music, String>, MusicRepositoryCustom {

    List<Music> findByEmbeddingIsNull();

    boolean existsByTitleAndSinger(String title, String singer);

    @Query("SELECT m FROM Music m WHERE LOWER(REPLACE(m.title, ' ', '')) = LOWER(REPLACE(m.title, ' ', ''))")
    Optional<Music> findByTitleIgnoreCaseContaining(String trim);


    List<Music> findAllByIdIn(List<String> id);
}
