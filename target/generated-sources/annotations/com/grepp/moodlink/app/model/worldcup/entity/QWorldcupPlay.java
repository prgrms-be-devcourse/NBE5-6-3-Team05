package com.grepp.moodlink.app.model.worldcup.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QWorldcupPlay is a Querydsl query type for WorldcupPlay
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QWorldcupPlay extends EntityPathBase<WorldcupPlay> {

    private static final long serialVersionUID = -2077079049L;

    public static final QWorldcupPlay worldcupPlay = new QWorldcupPlay("worldcupPlay");

    public final DatePath<java.time.LocalDate> createdAt = createDate("createdAt", java.time.LocalDate.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath userId = createString("userId");

    public final StringPath winnerId = createString("winnerId");

    public final NumberPath<Long> worldcupId = createNumber("worldcupId", Long.class);

    public QWorldcupPlay(String variable) {
        super(WorldcupPlay.class, forVariable(variable));
    }

    public QWorldcupPlay(Path<? extends WorldcupPlay> path) {
        super(path.getType(), path.getMetadata());
    }

    public QWorldcupPlay(PathMetadata metadata) {
        super(WorldcupPlay.class, metadata);
    }

}

