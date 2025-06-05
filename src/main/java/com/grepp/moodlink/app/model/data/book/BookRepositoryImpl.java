package com.grepp.moodlink.app.model.data.book;

import com.grepp.moodlink.app.model.data.book.dto.BookDto;
import com.grepp.moodlink.app.model.data.book.entity.QBook;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class BookRepositoryImpl implements BookRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    private final JPAQueryFactory queryFactory;
    private final QBook book = QBook.book;

    @Override
    public String findTopThumbnail() {
        return em.createQuery(
                "SELECT b.image FROM Book b ORDER BY b.likeCount DESC", String.class)
            .setMaxResults(1)
            .getSingleResult();
    }

    @Override
    public String findPeople() {
        return em.createQuery(
                "SELECT b.author FROM Book b ORDER BY b.likeCount DESC", String.class)
            .setMaxResults(1)
            .getSingleResult();
    }

    @Override
    public String findTitle() {
        return em.createQuery(
                "SELECT b.title FROM Book b ORDER BY b.likeCount DESC", String.class)
            .setMaxResults(1)
            .getSingleResult();
    }

    @Override
    public List<BookDto> searchContent(String contentName) {
        return queryFactory.select(Projections.constructor(BookDto.class,
                book.title,
                book.description,
                book.publishedDate,
                book.publisher,
                book.image))
            .from(book)
            .where(book.title.lower().like("%" + contentName.toLowerCase() + "%")
                .or(book.publisher.lower().like("%" + contentName.toLowerCase() + "%"))
            ).fetch();
    }
}