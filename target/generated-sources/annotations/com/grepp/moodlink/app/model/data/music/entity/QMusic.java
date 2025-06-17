package com.grepp.moodlink.app.model.data.music.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMusic is a Querydsl query type for Music
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMusic extends EntityPathBase<Music> {

    private static final long serialVersionUID = 444944121L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMusic music = new QMusic("music");

    public final com.grepp.moodlink.infra.entity.QBaseEntity _super = new com.grepp.moodlink.infra.entity.QBaseEntity(this);

    //inherited
    public final BooleanPath activated = _super.activated;

    public final DatePath<java.time.LocalDate> createdAt = createDate("createdAt", java.time.LocalDate.class);

    public final StringPath description = createString("description");

    public final ArrayPath<byte[], Byte> embedding = createArray("embedding", byte[].class);

    public final QMusicGenre genre;

    public final StringPath id = createString("id");

    public final NumberPath<Long> likeCount = createNumber("likeCount", Long.class);

    public final StringPath lyrics = createString("lyrics");

    public final DatePath<java.time.LocalDate> modifiedAt = createDate("modifiedAt", java.time.LocalDate.class);

    public final DatePath<java.time.LocalDate> releaseDate = createDate("releaseDate", java.time.LocalDate.class);

    public final StringPath singer = createString("singer");

    public final StringPath summary = createString("summary");

    public final StringPath thumbnail = createString("thumbnail");

    public final StringPath title = createString("title");

    public QMusic(String variable) {
        this(Music.class, forVariable(variable), INITS);
    }

    public QMusic(Path<? extends Music> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMusic(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMusic(PathMetadata metadata, PathInits inits) {
        this(Music.class, metadata, inits);
    }

    public QMusic(Class<? extends Music> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.genre = inits.isInitialized("genre") ? new QMusicGenre(forProperty("genre")) : null;
    }

}

