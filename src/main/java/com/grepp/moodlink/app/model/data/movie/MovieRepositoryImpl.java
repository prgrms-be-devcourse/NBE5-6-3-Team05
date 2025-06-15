package com.grepp.moodlink.app.model.data.movie;

import com.grepp.moodlink.app.model.data.movie.dto.MovieDto;
import com.grepp.moodlink.app.model.data.movie.entity.Movie;
import com.grepp.moodlink.app.model.data.movie.entity.QGenre;
import com.grepp.moodlink.app.model.data.movie.entity.QMovie;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MovieRepositoryImpl implements MovieRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    private final JPAQueryFactory queryFactory;
    private final QMovie movie = QMovie.movie;
    private final QGenre genre = QGenre.genre;


    @Override
    public String findTopThumbnail() {
        return em.createQuery(
                "SELECT m.thumbnail FROM Movie m ORDER BY m.likeCount DESC", String.class)
            .setMaxResults(1)
            .getSingleResult();
    }

    @Override
    public String findTitle() {
        return em.createQuery(
                "SELECT m.title FROM Movie m ORDER BY m.likeCount DESC", String.class)
            .setMaxResults(1)
            .getSingleResult();
    }

    @Override
    public List<MovieDto> searchContent(String contentName) {
        return queryFactory.select(Projections.constructor(MovieDto.class,
                movie.title,
                movie.summary,
                movie.releaseDate,
                movie.thumbnail))
            .from(movie)
            .where(movie.title.lower().like("%" + contentName.toLowerCase() + "%")).fetch();
    }

    @Override
    public Optional<Movie> findByIdWithGenre(String id) {

        JPAQuery<Movie> content = queryFactory
            .select(movie)
            .from(movie)
            .leftJoin(movie.genres).fetchJoin()
            .where(
                movie.id.eq(id),
                movie.activated
            );

        return Optional.ofNullable(content.fetchOne());
    }

    public List<MovieDto> searchContentByGenre(String genreName) {
        return queryFactory
            .select(Projections.constructor(MovieDto.class,
                movie.title,
                movie.summary,
                movie.releaseDate,
                movie.thumbnail))
            .from(movie)
            .join(movie.genres, genre)
            .where(genre.name.eq(genreName))
            .fetch();
    }
}