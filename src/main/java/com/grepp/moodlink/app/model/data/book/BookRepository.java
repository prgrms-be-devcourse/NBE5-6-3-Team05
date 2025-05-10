package com.grepp.moodlink.app.model.data.book;

import com.grepp.moodlink.app.model.data.book.entity.Book;
import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<Book, Long> {

}
