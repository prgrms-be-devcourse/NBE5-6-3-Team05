package com.grepp.moodlink.app.model.recomend.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QLikeDetailMusic is a Querydsl query type for LikeDetailMusic
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QLikeDetailMusic extends EntityPathBase<LikeDetailMusic> {

    private static final long serialVersionUID = 1369849413L;

    public static final QLikeDetailMusic likeDetailMusic = new QLikeDetailMusic("likeDetailMusic");

    public final QLikeDetailBase _super = new QLikeDetailBase(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final NumberPath<Long> likesId = _super.likesId;

    public final StringPath musicId = createString("musicId");

    public QLikeDetailMusic(String variable) {
        super(LikeDetailMusic.class, forVariable(variable));
    }

    public QLikeDetailMusic(Path<? extends LikeDetailMusic> path) {
        super(path.getType(), path.getMetadata());
    }

    public QLikeDetailMusic(PathMetadata metadata) {
        super(LikeDetailMusic.class, metadata);
    }

}

