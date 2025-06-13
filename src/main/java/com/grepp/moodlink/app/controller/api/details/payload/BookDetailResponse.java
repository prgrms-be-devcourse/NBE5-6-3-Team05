package com.grepp.moodlink.app.controller.api.details.payload;

import com.grepp.moodlink.app.model.data.music.entity.MusicGenre;
import com.grepp.moodlink.app.model.details.dto.SongDetailsDto;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class MusicDetailResponse {
    SongDetailsDto music;
    MusicGenre musicGenre;

}
