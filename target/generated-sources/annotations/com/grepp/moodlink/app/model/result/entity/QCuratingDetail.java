package com.grepp.moodlink.app.model.result.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCuratingDetail is a Querydsl query type for CuratingDetail
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCuratingDetail extends EntityPathBase<CuratingDetail> {

    private static final long serialVersionUID = -1162968408L;

    public static final QCuratingDetail curatingDetail = new QCuratingDetail("curatingDetail");

    public final StringPath bookId = createString("bookId");

    public final NumberPath<Long> curatingId = createNumber("curatingId", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath movieId = createString("movieId");

    public final StringPath songId = createString("songId");

    public QCuratingDetail(String variable) {
        super(CuratingDetail.class, forVariable(variable));
    }

    public QCuratingDetail(Path<? extends CuratingDetail> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCuratingDetail(PathMetadata metadata) {
        super(CuratingDetail.class, metadata);
    }

}

