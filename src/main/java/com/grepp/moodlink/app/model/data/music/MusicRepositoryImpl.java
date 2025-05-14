package com.grepp.moodlink.app.model.data.music;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class MusicRepositoryImpl implements MusicRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public String findTopThumbnail() {
        return em.createQuery(
                "SELECT s.thumbnail FROM Music s ORDER BY s.likeCount DESC", String.class)
            .setMaxResults(1)
            .getSingleResult();
    }

    @Override
    public String findPeople() {
        return em.createQuery("select s.singer from Music s ORDER BY s.likeCount DESC", String.class)
            .setMaxResults(1)
            .getSingleResult();
    }

    @Override
    public String findTitle() {
        return em.createQuery("select s.title from Music s ORDER BY s.likeCount DESC", String.class)
            .setMaxResults(1)
            .getSingleResult();
    }

    @Override
    public String findDescription() {
        return em.createQuery("select s.lyrics from Music s ORDER BY s.likeCount DESC", String.class)
            .setMaxResults(1)
            .getSingleResult();
    }
}
