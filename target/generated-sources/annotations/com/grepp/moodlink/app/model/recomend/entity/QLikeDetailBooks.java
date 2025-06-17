package com.grepp.moodlink.app.model.recomend.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QLikeDetailBooks is a Querydsl query type for LikeDetailBooks
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QLikeDetailBooks extends EntityPathBase<LikeDetailBooks> {

    private static final long serialVersionUID = 1359508170L;

    public static final QLikeDetailBooks likeDetailBooks = new QLikeDetailBooks("likeDetailBooks");

    public final QLikeDetailBase _super = new QLikeDetailBase(this);

    public final StringPath bookId = createString("bookId");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final NumberPath<Long> likesId = _super.likesId;

    public QLikeDetailBooks(String variable) {
        super(LikeDetailBooks.class, forVariable(variable));
    }

    public QLikeDetailBooks(Path<? extends LikeDetailBooks> path) {
        super(path.getType(), path.getMetadata());
    }

    public QLikeDetailBooks(PathMetadata metadata) {
        super(LikeDetailBooks.class, metadata);
    }

}

