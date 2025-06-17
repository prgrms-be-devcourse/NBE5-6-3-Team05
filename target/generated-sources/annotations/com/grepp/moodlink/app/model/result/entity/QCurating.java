package com.grepp.moodlink.app.model.result.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCurating is a Querydsl query type for Curating
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCurating extends EntityPathBase<Curating> {

    private static final long serialVersionUID = -989056137L;

    public static final QCurating curating = new QCurating("curating");

    public final DatePath<java.time.LocalDate> curatingDate = createDate("curatingDate", java.time.LocalDate.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath userId = createString("userId");

    public QCurating(String variable) {
        super(Curating.class, forVariable(variable));
    }

    public QCurating(Path<? extends Curating> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCurating(PathMetadata metadata) {
        super(Curating.class, metadata);
    }

}

