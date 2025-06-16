package com.grepp.moodlink.app.model.admin.movie.repository;

import com.grepp.moodlink.app.model.data.movie.entity.Movie;
import com.grepp.moodlink.app.model.data.movie.entity.QMovie;
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

@Slf4j
@Repository
@RequiredArgsConstructor
public class AdminMovieRepositoryImpl implements AdminMovieRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    private final JPAQueryFactory queryFactory;
    private final QMovie movie = QMovie.movie;

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
    public Long countMoviesByGenre(Integer id) {
        return queryFactory
            .select(movie.count())
            .from(movie)
            .where(movie.genres.any().id.eq(id))
            .fetchOne();
    }

}