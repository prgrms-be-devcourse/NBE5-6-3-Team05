package com.grepp.moodlink.app.model.data.movie;

import com.grepp.moodlink.app.model.data.movie.dto.MovieDto;
import com.grepp.moodlink.app.model.data.movie.dto.MovieInfoDto;
import com.grepp.moodlink.app.model.data.movie.entity.Movie;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MovieRepositoryImpl implements MovieRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    private final JPAQueryFactory queryFactory;
    private final QMovie movie = QMovie.movie;

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
    public Page<Movie> findPaged(Pageable pageable) {

        List<Movie> content = queryFactory
            .select(movie)
            .from(movie)
            .leftJoin(movie.genres).fetchJoin()
            .where(movie.activated)
            .orderBy(movie.modifiedAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPAQuery<Long> countQuery = queryFactory
            .select(movie.count())
            .from(movie)
            .leftJoin(movie.genres)
            .where(movie.activated);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
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

    @Override
    @Transactional
    public void updateBook(MovieInfoDto dto) {
        Movie entity = em.find(Movie.class, dto.getId());

        if (entity == null) {
            log.warn("영화 없음: {}", dto.getId());
        }

        assert entity != null;
        entity.setGenres(dto.getGenres());
        if(dto.getThumbnail()!=null){
            entity.setThumbnail(dto.getThumbnail());
        }
        entity.setDescription(dto.getDescription());
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
}