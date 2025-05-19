package com.grepp.moodlink.app.model.data.book;

import com.grepp.moodlink.app.model.data.book.dto.BookDto;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepositoryCustom {

    String findTopThumbnail();

    String findPeople();

    String findTitle();

    List<BookDto> searchContent(String contentName);
}
