package com.grepp.moodlink.app.model.worldcup.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QWorldcup is a Querydsl query type for Worldcup
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QWorldcup extends EntityPathBase<Worldcup> {

    private static final long serialVersionUID = -188148701L;

    public static final QWorldcup worldcup = new QWorldcup("worldcup");

    public final EnumPath<com.grepp.moodlink.app.model.worldcup.code.ContentType> contentType = createEnum("contentType", com.grepp.moodlink.app.model.worldcup.code.ContentType.class);

    public final DatePath<java.time.LocalDate> createdAt = createDate("createdAt", java.time.LocalDate.class);

    public final StringPath hashCode = createString("hashCode");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imageUrl = createString("imageUrl");

    public final StringPath title = createString("title");

    public final StringPath userId = createString("userId");

    public QWorldcup(String variable) {
        super(Worldcup.class, forVariable(variable));
    }

    public QWorldcup(Path<? extends Worldcup> path) {
        super(path.getType(), path.getMetadata());
    }

    public QWorldcup(PathMetadata metadata) {
        super(Worldcup.class, metadata);
    }

}

