package com.grepp.moodlink.app.model.result.dto;

import com.grepp.moodlink.app.model.data.book.entity.Book;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {
    private String id;
    private String name;
    private String imgUrl;
    private String externalLink;
    private boolean status;

    public BookDto(String id, String name, String imgUrl) {
        this.id = id;
        this.name = name;
        this.imgUrl = imgUrl;
    }

    public static BookDto from(Book book){
        return new BookDto(book.getIsbn(), book.getTitle(), book.getImage());
    }
}
