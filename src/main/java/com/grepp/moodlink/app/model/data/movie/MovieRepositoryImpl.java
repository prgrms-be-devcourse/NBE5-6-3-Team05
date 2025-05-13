package com.grepp.moodlink.app.model.data.movie;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class MovieRepositoryImpl implements MovieRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public String findTopThumbnail() {
        return em.createQuery(
                "SELECT m.thumbnail FROM Movie m ORDER BY m.likeCount DESC", String.class)
            .setMaxResults(1)
            .getSingleResult();
    }
}