package com.grepp.moodlink.app.controller.api.details.payload;

import com.grepp.moodlink.app.model.data.music.dto.MusicGenreDto;
import com.grepp.moodlink.app.model.details.dto.SongDetailsDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class MusicDetailResponse {
    private SongDetailsDto music;
    private MusicGenreDto musicGenre;

}
