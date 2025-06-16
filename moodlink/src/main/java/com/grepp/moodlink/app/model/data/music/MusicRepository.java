package com.grepp.moodlink.app.model.data.music;

import com.grepp.moodlink.app.model.data.music.entity.Music;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MusicRepository extends JpaRepository<Music, String>, MusicRepositoryCustom {

    List<Music> findByEmbeddingIsNull();

    @Query("SELECT m.id FROM Music m where m.title = :title")
    Optional<String> findIdByTitle(@Param("title") String s);


    List<Music> findAllByIdIn(List<String> id);

    List<Music> findTop10ByOrderByLikeCountDesc();
}
