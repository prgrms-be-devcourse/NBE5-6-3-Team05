package com.grepp.moodlink.app.model.admin.book;

import com.grepp.moodlink.app.model.data.book.entity.Book;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AdminBookRepository extends JpaRepository<Book, String>,
    AdminBookRepositoryCustom {

    boolean existsByTitleAndAuthor(String title, String author);

    Book findByIsbn(String isbn);
}
