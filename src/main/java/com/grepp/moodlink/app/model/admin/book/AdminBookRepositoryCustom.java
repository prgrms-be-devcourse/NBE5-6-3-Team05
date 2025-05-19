package com.grepp.moodlink.app.model.admin.book;

import com.grepp.moodlink.app.model.data.book.dto.BookDto;
import com.grepp.moodlink.app.model.data.book.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminBookRepositoryCustom {

    void updateBook(BookDto book);

    Page<Book> findPaged(Pageable pageable);
}
