package com.grepp.moodlink.app.model.data.music;

import com.grepp.moodlink.app.model.data.book.entity.Book;
import com.grepp.moodlink.app.model.data.music.entity.Music;
import com.grepp.moodlink.app.model.data.music.entity.QMusic;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class MusicRepositoryImpl implements MusicRepositoryCustom {

    @PersistenceContext
    private EntityManager em;


    private final JPAQueryFactory queryFactory;
    private final QMusic music = QMusic.music;


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

    @Override
    public Page<Music> findPaged(Pageable pageable) {

        List<Music> content = queryFactory
            .select(music)
            .from(music)
            .where(music.activated)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPAQuery<Long> countQuery = queryFactory
            .select(music.count())
            .from(music)
            .where(music.activated);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }
}
