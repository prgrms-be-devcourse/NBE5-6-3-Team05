package com.grepp.moodlink.app.model.result.dto;


import lombok.Data;

@Data
public class CuratingDetailDto {
    private BookSimpleDto book;
    private MovieSimpleDto movie;
    private SongSimpleDto song;

    public CuratingDetailDto(BookSimpleDto book, SongSimpleDto song, MovieSimpleDto movie) {
        this.book = book;
        this.movie = movie;
        this.song = song;
    }

}
