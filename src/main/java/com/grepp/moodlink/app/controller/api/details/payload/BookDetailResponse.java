package com.grepp.moodlink.app.controller.api.details.payload;

import com.grepp.moodlink.app.model.data.book.dto.BookGenreDto;
import com.grepp.moodlink.app.model.details.dto.BookDetailsDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDetailResponse {
    private BookDetailsDto book;
    private BookGenreDto bookGenre;

}
