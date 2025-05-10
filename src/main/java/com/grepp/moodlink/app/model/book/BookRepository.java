package com.grepp.moodlink.app.model.book;

import com.grepp.moodlink.app.model.book.entity.Book;
import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<Book, Long> {

}
