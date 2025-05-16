package com.grepp.moodlink.app.model.data.music;

import com.grepp.moodlink.app.model.data.music.entity.Music;
import com.grepp.moodlink.app.model.result.dto.SongDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MusicRepository extends JpaRepository<Music, String> {

    List<Music> findByEmbeddingIsNull();

    @Query("SELECT new com.grepp.moodlink.app.model.result.dto.SongDto(m.id, m.title, m.thumbnail) " +
        "FROM Music m WHERE m.id = :id")
    Optional<SongDto> findSimpleById(@Param("id") String id);

    @Query("SELECT m.id FROM Music m where m.title = :title")
    Optional<String> findIdByTitle(@Param("title")String s);
}
