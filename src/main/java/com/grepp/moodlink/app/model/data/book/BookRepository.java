package com.grepp.moodlink.app.model.data.book;

import com.grepp.moodlink.app.model.data.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, String> {

    List<Book> findByEmbeddingIsNull();
}
