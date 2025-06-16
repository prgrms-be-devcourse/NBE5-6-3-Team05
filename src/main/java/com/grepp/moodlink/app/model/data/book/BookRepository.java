package com.grepp.moodlink.app.model.data.book;

import com.grepp.moodlink.app.model.data.book.entity.Book;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<Book, String>, BookRepositoryCustom {

    List<Book> findByEmbeddingIsNull();

    Book findByIsbn(String isbn);

    @Query("SELECT b.isbn FROM Book b where b.title = :title")
    Optional<String> findIsbnByTitle(@Param("title") String title);

    List<Book> findAllByIsbnIn(List<String> isbn);
}
