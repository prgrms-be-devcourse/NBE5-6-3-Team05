package com.grepp.moodlink.app.model.data.book.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBookGenre is a Querydsl query type for BookGenre
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBookGenre extends EntityPathBase<BookGenre> {

    private static final long serialVersionUID = 1354699454L;

    public static final QBookGenre bookGenre = new QBookGenre("bookGenre");

    public final BooleanPath activated = createBoolean("activated");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public QBookGenre(String variable) {
        super(BookGenre.class, forVariable(variable));
    }

    public QBookGenre(Path<? extends BookGenre> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBookGenre(PathMetadata metadata) {
        super(BookGenre.class, metadata);
    }

}

