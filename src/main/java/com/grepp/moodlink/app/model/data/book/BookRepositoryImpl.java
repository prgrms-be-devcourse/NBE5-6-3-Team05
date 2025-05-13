package com.grepp.moodlink.app.model.data.book;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

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
}