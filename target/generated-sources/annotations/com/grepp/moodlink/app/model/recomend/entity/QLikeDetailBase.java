package com.grepp.moodlink.app.model.recomend.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QLikeDetailBase is a Querydsl query type for LikeDetailBase
 */
@Generated("com.querydsl.codegen.DefaultSupertypeSerializer")
public class QLikeDetailBase extends EntityPathBase<LikeDetailBase> {

    private static final long serialVersionUID = -1618726223L;

    public static final QLikeDetailBase likeDetailBase = new QLikeDetailBase("likeDetailBase");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> likesId = createNumber("likesId", Long.class);

    public QLikeDetailBase(String variable) {
        super(LikeDetailBase.class, forVariable(variable));
    }

    public QLikeDetailBase(Path<? extends LikeDetailBase> path) {
        super(path.getType(), path.getMetadata());
    }

    public QLikeDetailBase(PathMetadata metadata) {
        super(LikeDetailBase.class, metadata);
    }

}

