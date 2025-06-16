package com.grepp.moodlink.app.model.data.fortune.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QFortune is a Querydsl query type for Fortune
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFortune extends EntityPathBase<Fortune> {

    private static final long serialVersionUID = 2041364729L;

    public static final QFortune fortune = new QFortune("fortune");

    public final StringPath content = createString("content");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QFortune(String variable) {
        super(Fortune.class, forVariable(variable));
    }

    public QFortune(Path<? extends Fortune> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFortune(PathMetadata metadata) {
        super(Fortune.class, metadata);
    }

}

