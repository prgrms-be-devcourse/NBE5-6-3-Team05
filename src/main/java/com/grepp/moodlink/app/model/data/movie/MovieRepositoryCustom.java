package com.grepp.moodlink.app.model.data.movie;

import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepositoryCustom {

    String findTopThumbnail();

    String findTitle();

    String findDescription();
}
