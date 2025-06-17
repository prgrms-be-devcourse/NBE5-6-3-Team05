package com.grepp.moodlink.app.model.data.music.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMusicGenre is a Querydsl query type for MusicGenre
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMusicGenre extends EntityPathBase<MusicGenre> {

    private static final long serialVersionUID = 2006597130L;

    public static final QMusicGenre musicGenre = new QMusicGenre("musicGenre");

    public final BooleanPath activated = createBoolean("activated");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public QMusicGenre(String variable) {
        super(MusicGenre.class, forVariable(variable));
    }

    public QMusicGenre(Path<? extends MusicGenre> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMusicGenre(PathMetadata metadata) {
        super(MusicGenre.class, metadata);
    }

}

