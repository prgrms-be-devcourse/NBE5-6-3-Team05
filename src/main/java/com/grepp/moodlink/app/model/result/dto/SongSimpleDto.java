package com.grepp.moodlink.app.model.result.dto;

import com.grepp.moodlink.app.model.data.music.dto.MusicDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SongSimpleDto {

    private String id;
    private String name;
    private String imgUrl;
    private String externalLink;
    private boolean status;
    private Long likeCount;

    public SongSimpleDto(String id, String name, String imgUrl, Long likeCount) {
        this.id = id;
        this.name = name;
        this.imgUrl = imgUrl;
        this.likeCount = likeCount;
        this.status = false;
        this.externalLink = "https://www.google.com/search?q=" + name;
    }


    public static SongSimpleDto from(MusicDto music) {
        return new SongSimpleDto(music.getId(), music.getTitle(), music.getThumbnail(), music.getLikeCount());
    }
}
