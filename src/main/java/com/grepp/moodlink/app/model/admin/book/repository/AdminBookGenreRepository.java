package com.grepp.moodlink.app.model.admin.book.repository;

import com.grepp.moodlink.app.model.data.book.entity.BookGenre;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminBookGenreRepository extends JpaRepository<BookGenre, Long> {

    BookGenre findByName(String genre);

    List<BookGenre> findAllByActivated(Boolean activated);
}
