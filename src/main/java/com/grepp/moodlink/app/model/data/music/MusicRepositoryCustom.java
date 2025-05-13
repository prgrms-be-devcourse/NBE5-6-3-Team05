package com.grepp.moodlink.app.model.data.music;

import org.springframework.stereotype.Repository;

@Repository
public interface MusicRepositoryCustom {

    String findTopThumbnail();

}
