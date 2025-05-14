package com.grepp.moodlink.app.model.result.dto;


import lombok.Data;

@Data
public class CuratingDetailDto {
    private BookDto book;
    private MovieDto movie;
    private SongDto song;

    public CuratingDetailDto(BookDto book, SongDto song, MovieDto movie) {
        this.book = book;
        this.movie = movie;
        this.song = song;
    }
}
