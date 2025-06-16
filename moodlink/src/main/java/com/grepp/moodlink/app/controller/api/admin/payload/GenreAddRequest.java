package com.grepp.moodlink.app.controller.api.admin.payload;

import com.grepp.moodlink.app.model.data.book.dto.BookGenreDto;
import com.grepp.moodlink.app.model.data.movie.dto.GenreDto;
import com.grepp.moodlink.app.model.data.music.dto.MusicGenreDto;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class GenreAddRequest {

    @NotEmpty
    private String name;
    private String contentType;


    public GenreDto toGenreDto() {
        GenreDto genreDto = new GenreDto();
        genreDto.setName(name);
        return genreDto;
    }

    public MusicGenreDto toMusicGenreDto() {
        MusicGenreDto musicGenreDto = new MusicGenreDto();
        musicGenreDto.setName(name);
        return musicGenreDto;
    }

    public BookGenreDto toBookGenreDto() {
        BookGenreDto bookGenreDto = new BookGenreDto();
        bookGenreDto.setName(name);
        return bookGenreDto;
    }
}
