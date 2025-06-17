package com.grepp.moodlink.app.model.recomend.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QLikes is a Querydsl query type for Likes
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QLikes extends EntityPathBase<Likes> {

    private static final long serialVersionUID = -619751484L;

    public static final QLikes likes = new QLikes("likes");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> totalCount = createNumber("totalCount", Long.class);

    public final StringPath userId = createString("userId");

    public QLikes(String variable) {
        super(Likes.class, forVariable(variable));
    }

    public QLikes(Path<? extends Likes> path) {
        super(path.getType(), path.getMetadata());
    }

    public QLikes(PathMetadata metadata) {
        super(Likes.class, metadata);
    }

}

