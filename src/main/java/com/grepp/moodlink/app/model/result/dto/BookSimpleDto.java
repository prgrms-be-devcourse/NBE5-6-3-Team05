package com.grepp.moodlink.app.model.result.dto;

import com.grepp.moodlink.app.model.data.book.dto.BookDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookSimpleDto {

    private String id;
    private String name;
    private String imgUrl;
    private String externalLink;
    private boolean status;

    public BookSimpleDto(String id, String name, String imgUrl) {
        this.id = id;
        this.name = name;
        this.imgUrl = imgUrl;
        this.status = false;
        this.externalLink = "https://www.google.com/search?q=" + name;
    }

    public static BookSimpleDto from(BookDto book) {
        return new BookSimpleDto(book.getIsbn(), book.getTitle(), book.getImage());
    }
}
