package com.grepp.moodlink.app.model.admin.music;

import com.grepp.moodlink.app.model.data.music.dto.MusicDto;
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
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Repository
public class AdminMusicRepositoryImpl implements AdminMusicRepositoryCustom {

    @PersistenceContext
    private EntityManager em;


    private final JPAQueryFactory queryFactory;
    private final QMusic music = QMusic.music;

    @Override
    public Page<Music> findPaged(Pageable pageable) {

        List<Music> content = queryFactory
            .select(music)
            .from(music)
            .where(music.activated)
            .orderBy(music.modifiedAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPAQuery<Long> countQuery = queryFactory
            .select(music.count())
            .from(music)
            .where(music.activated);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    @Transactional
    public void updateBook(MusicDto music) {
        Music entity = em.find(Music.class, music.getId());

        entity.setGenre(music.getGenre());
        if (music.getThumbnail() != null) {
            entity.setThumbnail(music.getThumbnail());
        }
        entity.setGenre(music.getGenre());
        entity.setDescription(music.getDescription());
        entity.setLyrics(music.getLyrics());
    }
}
