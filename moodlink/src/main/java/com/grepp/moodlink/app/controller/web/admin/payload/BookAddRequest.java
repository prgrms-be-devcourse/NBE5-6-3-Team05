package com.grepp.moodlink.app.controller.web.admin.payload;

import com.grepp.moodlink.app.model.data.book.dto.BookDto;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class BookAddRequest {
    // 직접 관리자가 컨텐츠를 추가하는 경우 파일로 썸네일을 받음
    private List<MultipartFile> image;
    // api를 통해 정보를 받아와 추가하는 경우 url 경로로 썸네일을 받음
    private String imagePath;
    @NotBlank
    private String title;
    @NotBlank
    private String author;
    @NotBlank
    private String genre;

    private String publisher;
    @NotBlank
    private String description;

    //front에서 LocalDate로 넘어옴
    private LocalDate publishedDate;

    public BookDto toDto() {
        BookDto bookDto = new BookDto();
        bookDto.setTitle(title);
        bookDto.setAuthor(author);
        bookDto.setGenre(genre);
        bookDto.setPublisher(publisher);
        bookDto.setDescription(description);
        bookDto.setPublishedDate(publishedDate);
        bookDto.setImage(imagePath);
        return bookDto;
    }
}
