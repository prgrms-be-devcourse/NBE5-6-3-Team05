package com.grepp.moodlink.app.model.data.music;

import com.grepp.moodlink.app.model.data.book.BookRepositoryCustom;
import com.grepp.moodlink.app.model.data.music.entity.Music;
import java.nio.channels.FileChannel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MusicRepository extends JpaRepository<Music, String>, MusicRepositoryCustom {

    List<Music> findByEmbeddingIsNull();
}
