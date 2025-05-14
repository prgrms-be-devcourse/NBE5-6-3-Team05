package com.grepp.moodlink.app.model.data.book;

import com.grepp.moodlink.app.model.data.book.dto.BookDto;
import com.grepp.moodlink.app.model.data.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByEmbeddingIsNull();

    boolean existsByTitleAndAuthor(String title, String author);

    Book findByIsbn(String isbn);

    List<Book> findByActivated(Boolean activated);
}
