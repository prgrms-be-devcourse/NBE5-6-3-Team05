package com.grepp.moodlink.app.model.admin.book.repository;

import com.grepp.moodlink.app.model.data.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminBookRepository extends JpaRepository<Book, String>,
    AdminBookRepositoryCustom {

    boolean existsByTitleAndAuthor(String title, String author);

    Book findByIsbn(String isbn);

    Long countBooksByGenre_Id(Long genreId);
}
