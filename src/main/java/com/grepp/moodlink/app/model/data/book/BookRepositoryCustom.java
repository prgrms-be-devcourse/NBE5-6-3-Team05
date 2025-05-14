package com.grepp.moodlink.app.model.data.book;

import org.springframework.stereotype.Repository;

@Repository
public interface BookRepositoryCustom {

    String findTopThumbnail();

    String findPeople();

    String findTitle();

    String findDescription();
}
