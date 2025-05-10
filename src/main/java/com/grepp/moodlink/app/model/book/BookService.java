package com.grepp.moodlink.app.model.book;

import com.grepp.moodlink.app.model.book.dto.BookDto;
import com.grepp.moodlink.app.model.book.entity.Book;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    public void saveMusic(List<BookDto> bookDtos) {

        for (BookDto dto : bookDtos) {
            Book book = new Book();
            book.setIsbn(String.valueOf(dto.getIsbn()));
            book.setTitle(dto.getTitle());
            book.setImage(String.valueOf(dto.getImage()));
            book.setAuthor(dto.getAuthor());
            book.setPublisher(dto.getPublisher());
            book.setPublishedDate(dto.getPublishedDate());
            book.setDescription(dto.getDescription());
            book.setGenre(dto.getGenre());

            bookRepository.save(book);
        }
    }

}
