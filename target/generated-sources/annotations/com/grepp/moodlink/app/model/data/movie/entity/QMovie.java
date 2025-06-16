package com.grepp.moodlink.app.model.data.movie.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMovie is a Querydsl query type for Movie
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMovie extends EntityPathBase<Movie> {

    private static final long serialVersionUID = -1627875111L;

    public static final QMovie movie = new QMovie("movie");

    public final com.grepp.moodlink.infra.entity.QBaseEntity _super = new com.grepp.moodlink.infra.entity.QBaseEntity(this);

    public final BooleanPath activated = createBoolean("activated");

    public final DatePath<java.time.LocalDate> createdAt = createDate("createdAt", java.time.LocalDate.class);

    public final StringPath description = createString("description");

    public final ArrayPath<byte[], Byte> embedding = createArray("embedding", byte[].class);

    public final SetPath<Genre, QGenre> genres = this.<Genre, QGenre>createSet("genres", Genre.class, QGenre.class, PathInits.DIRECT2);

    public final StringPath id = createString("id");

    public final NumberPath<Long> likeCount = createNumber("likeCount", Long.class);

    public final DatePath<java.time.LocalDate> modifiedAt = createDate("modifiedAt", java.time.LocalDate.class);

    public final DatePath<java.time.LocalDate> releaseDate = createDate("releaseDate", java.time.LocalDate.class);

    public final StringPath summary = createString("summary");

    public final StringPath thumbnail = createString("thumbnail");

    public final StringPath title = createString("title");

    public QMovie(String variable) {
        super(Movie.class, forVariable(variable));
    }

    public QMovie(Path<? extends Movie> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMovie(PathMetadata metadata) {
        super(Movie.class, metadata);
    }

}

