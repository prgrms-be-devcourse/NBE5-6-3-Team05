package com.grepp.moodlink.app.model.data.music;

import com.grepp.moodlink.app.model.data.music.dto.MusicDto;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface MusicRepositoryCustom {

    String findTopThumbnail();

    String findPeople();

    String findTitle();

    List<MusicDto> searchContent(String contentName);
}
