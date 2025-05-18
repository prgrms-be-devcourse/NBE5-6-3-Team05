package com.grepp.moodlink.app.model.result.dto;

import com.grepp.moodlink.app.model.data.music.entity.Music;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SongDto {
    private String id;
    private String name;
    private String imgUrl;
    private String externalLink;
    private boolean status;

    public SongDto(String id, String name, String imgUrl) {
        this.id = id;
        this.name = name;
        this.imgUrl = imgUrl;
    }


    public static SongDto from(Music music){
        return new SongDto(music.getId(), music.getTitle(), music.getThumbnail());
    }
}
