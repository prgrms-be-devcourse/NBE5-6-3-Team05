package com.grepp.moodlink.app.model.data.book.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBook is a Querydsl query type for Book
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBook extends EntityPathBase<Book> {

    private static final long serialVersionUID = -1699522107L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBook book = new QBook("book");

    public final com.grepp.moodlink.infra.entity.QBaseEntity _super = new com.grepp.moodlink.infra.entity.QBaseEntity(this);

    //inherited
    public final BooleanPath activated = _super.activated;

    public final StringPath author = createString("author");

    public final DatePath<java.time.LocalDate> createdAt = createDate("createdAt", java.time.LocalDate.class);

    public final StringPath description = createString("description");

    public final ArrayPath<byte[], Byte> embedding = createArray("embedding", byte[].class);

    public final QBookGenre genre;

    public final StringPath image = createString("image");

    public final StringPath isbn = createString("isbn");

    public final NumberPath<Long> likeCount = createNumber("likeCount", Long.class);

    public final DatePath<java.time.LocalDate> modifiedAt = createDate("modifiedAt", java.time.LocalDate.class);

    public final DatePath<java.time.LocalDate> publishedDate = createDate("publishedDate", java.time.LocalDate.class);

    public final StringPath publisher = createString("publisher");

    public final StringPath summary = createString("summary");

    public final StringPath title = createString("title");

    public QBook(String variable) {
        this(Book.class, forVariable(variable), INITS);
    }

    public QBook(Path<? extends Book> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBook(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBook(PathMetadata metadata, PathInits inits) {
        this(Book.class, metadata, inits);
    }

    public QBook(Class<? extends Book> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.genre = inits.isInitialized("genre") ? new QBookGenre(forProperty("genre")) : null;
    }

}

