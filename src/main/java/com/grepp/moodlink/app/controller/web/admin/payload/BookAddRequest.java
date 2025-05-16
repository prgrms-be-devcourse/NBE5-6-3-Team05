package com.grepp.moodlink.app.controller.web.admin.payload;

import com.grepp.moodlink.app.model.data.book.dto.BookDto;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class BookAddRequest {

    private List<MultipartFile> image;
    @NotBlank
    private String title;
    @NotBlank
    private String author;
    @NotBlank
    private String genre;

    private String publisher;

    private String description;

    //front에서 LocalDate로 넘어옴
    private LocalDate publishedDate;

    public BookDto toDto(){
        BookDto bookDto= new BookDto();
        bookDto.setTitle(title);
        bookDto.setAuthor(author);
        bookDto.setGenre(genre);
        bookDto.setPublisher(publisher);
        bookDto.setDescription(description);
        bookDto.setPublishedDate(publishedDate);
        return bookDto;
    }
}
