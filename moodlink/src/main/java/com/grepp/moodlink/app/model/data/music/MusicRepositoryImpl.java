package com.grepp.moodlink.app.model.data.music;

import com.grepp.moodlink.app.model.data.music.dto.MusicDto;
import com.grepp.moodlink.app.model.data.music.entity.QMusic;
import com.grepp.moodlink.app.model.data.music.entity.QMusicGenre;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class MusicRepositoryImpl implements MusicRepositoryCustom {

    @PersistenceContext
    private EntityManager em;


    private final JPAQueryFactory queryFactory;
    private final QMusic music = QMusic.music;
    private final QMusicGenre genre = QMusicGenre.musicGenre;



    @Override
    public String findTopThumbnail() {
        return em.createQuery(
                "SELECT s.thumbnail FROM Music s ORDER BY s.likeCount DESC", String.class)
            .setMaxResults(1)
            .getSingleResult();
    }

    @Override
    public String findPeople() {
        return em.createQuery("select s.singer from Music s ORDER BY s.likeCount DESC",
                String.class)
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
    public List<MusicDto> searchContent(String contentName) {
        return queryFactory.select(Projections.constructor(MusicDto.class,
                music.id,
                music.title,
                music.singer,
                music.thumbnail,
                music.releaseDate,
                music.lyrics))
            .from(music)
            .where(
                music.title.lower().like("%" + contentName.toLowerCase() + "%")
                    .or(music.singer.lower().like("%" + contentName.toLowerCase() + "%"))
            ).fetch();
    }

    @Override
    public List<MusicDto> searchContentByGenre(String genreName) {
        return queryFactory
            .select(Projections.constructor(MusicDto.class,
                music.title,
                music.singer,
                music.thumbnail,
                music.releaseDate,
                music.lyrics))
            .from(music)
            .join(music.genre, genre)
            .where(genre.name.eq(genreName))
            .fetch();
    }
}
