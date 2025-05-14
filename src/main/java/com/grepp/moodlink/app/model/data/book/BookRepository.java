package com.grepp.moodlink.app.model.data.book;

import com.grepp.moodlink.app.model.data.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, String> {

    List<Book> findByEmbeddingIsNull();

    @Query("SELECT b FROM Book b WHERE LOWER(REPLACE(b.title, ' ', '')) = LOWER(REPLACE(b.title, ' ', ''))")
    Optional<Book> findByTitleIgnoreCaseContaining(String trim);
}
