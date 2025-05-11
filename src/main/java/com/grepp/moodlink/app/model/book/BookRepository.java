package com.grepp.moodlink.app.model.book;

import com.grepp.moodlink.app.model.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book,String> {

}
