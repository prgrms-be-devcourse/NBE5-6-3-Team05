package com.grepp.moodlink.app.model.data.book;

import com.grepp.moodlink.app.model.data.book.dto.BookDto;
import com.grepp.moodlink.app.model.data.book.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface BookRepositoryCustom {

    String findTopThumbnail();

    String findPeople();

    String findTitle();

    String findDescription();

    void updateBook(BookDto book);

    Page<Book> findPaged(Pageable pageable);
}
