package com.grepp.moodlink.app.model.recomend.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QLikeDetailMovies is a Querydsl query type for LikeDetailMovies
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QLikeDetailMovies extends EntityPathBase<LikeDetailMovies> {

    private static final long serialVersionUID = -489792733L;

    public static final QLikeDetailMovies likeDetailMovies = new QLikeDetailMovies("likeDetailMovies");

    public final QLikeDetailBase _super = new QLikeDetailBase(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final NumberPath<Long> likesId = _super.likesId;

    public final StringPath movieId = createString("movieId");

    public QLikeDetailMovies(String variable) {
        super(LikeDetailMovies.class, forVariable(variable));
    }

    public QLikeDetailMovies(Path<? extends LikeDetailMovies> path) {
        super(path.getType(), path.getMetadata());
    }

    public QLikeDetailMovies(PathMetadata metadata) {
        super(LikeDetailMovies.class, metadata);
    }

}

