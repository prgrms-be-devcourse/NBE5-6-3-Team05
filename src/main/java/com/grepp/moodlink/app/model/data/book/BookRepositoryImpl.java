package com.grepp.moodlink.app.model.data.book;

import com.grepp.moodlink.app.model.data.book.dto.BookDto;
import com.grepp.moodlink.app.model.data.book.entity.Book;
import com.grepp.moodlink.app.model.data.book.entity.QBook;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public void updateBook(BookDto book) {
        Book entity = em.find(Book.class, book.getIsbn());

        if (entity == null) {
            log.warn("도서 없음: {}", book.getIsbn());
        }

        entity.setGenre(book.getGenre());
        entity.setImage(book.getImage());
        entity.setPublisher(book.getPublisher());
        entity.setPublishedDate(book.getPublishedDate());
        entity.setDescription(book.getDescription());
    }

    @Override
    public Page<Book> findPaged(Pageable pageable) {

        List<Book> content = queryFactory
            .select(book)
            .from(book)
            .where(book.activated)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPAQuery<Long> countQuery = queryFactory
            .select(book.count())
            .from(book)
            .where(book.activated);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }
}