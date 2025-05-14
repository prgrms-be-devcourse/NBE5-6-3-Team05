package com.grepp.moodlink.app.model.data.book;

import com.grepp.moodlink.app.model.data.book.dto.BookDto;
import com.grepp.moodlink.app.model.data.book.entity.Book;
import com.grepp.moodlink.infra.error.exceptions.CommonException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Repository
public class BookRepositoryImpl implements BookRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public String findTopThumbnail() {
        return em.createQuery(
                "SELECT b.image FROM Book b ORDER BY b.likeCount DESC", String.class)
            .setMaxResults(1)
            .getSingleResult();
    }

    @Override
    @Transactional
    public void updateBook(BookDto book) {
        Book entity = em.find(Book.class, book.getIsbn());

        if (entity == null) {
            log.warn("도서 없음: {}", book.getIsbn());
        }

        log.info("도서 수정 전: {}", entity);

        entity.setGenre(book.getGenre());
        if(book.getImage()!=null){
            entity.setImage(book.getImage());
        }
        if(book.getPublisher()!=null){
            entity.setPublisher(book.getPublisher());
        }
        if(book.getPublishedDate()!=null){
            entity.setPublishedDate(book.getPublishedDate());
        }
        if(book.getDescription()!=null){
            entity.setDescription(book.getDescription());
        }

        log.info("도서 수정 후: {}", entity);
    }
}