package com.grepp.moodlink.app.model.admin.music.repository;

import com.grepp.moodlink.app.model.data.music.entity.Music;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminMusicRepositoryCustom {

    Page<Music> findPaged(Pageable pageable);
}
