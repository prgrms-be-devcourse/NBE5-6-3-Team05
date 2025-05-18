package com.grepp.moodlink.app.model.data.book;

import com.grepp.moodlink.app.model.data.book.dto.BookDto;
import com.grepp.moodlink.app.model.data.book.entity.Book;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepositoryCustom {

    String findTopThumbnail();

    String findPeople();

    String findTitle();

    void updateBook(BookDto book);

    Page<Book> findPaged(Pageable pageable);

    List<BookDto> searchContent(String contentName);
}
