package com.grepp.moodlink.app.model.data.book;

import com.grepp.moodlink.app.model.result.dto.BookDto;
import com.grepp.moodlink.app.model.data.book.entity.Book;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<Book, String>, BookRepositoryCustom {

    List<Book> findByEmbeddingIsNull();

    boolean existsByTitleAndAuthor(String title, String author);

    Book findByIsbn(String isbn);

    List<Book> findByActivated(Boolean activated);

    @Query("SELECT new com.grepp.moodlink.app.model.result.dto.BookDto(b.isbn,b.title, b.image) " +
        "FROM Book b WHERE b.isbn = :isbn")
    Optional<BookDto> findSimpleByIsbn(@Param("isbn") String isbn);
}
