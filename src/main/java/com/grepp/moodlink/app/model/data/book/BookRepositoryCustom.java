package com.grepp.moodlink.app.model.data.book;

import com.grepp.moodlink.app.model.data.book.dto.BookDto;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface BookRepositoryCustom {

    String findTopThumbnail();

    void updateBook(BookDto book);
}
