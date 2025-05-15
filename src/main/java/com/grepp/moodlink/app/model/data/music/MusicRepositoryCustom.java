package com.grepp.moodlink.app.model.data.music;

import com.grepp.moodlink.app.model.data.music.dto.MusicDto;
import com.grepp.moodlink.app.model.data.music.entity.Music;
import java.nio.channels.FileChannel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface MusicRepositoryCustom {

    String findTopThumbnail();

    String findPeople();

    String findTitle();

    String findDescription();

    Page<Music> findPaged(Pageable pageable);

    void updateBook(MusicDto dto);
}
