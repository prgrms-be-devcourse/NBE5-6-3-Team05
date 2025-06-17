package com.grepp.moodlink.app.model.keyword.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QKeywordSelection is a Querydsl query type for KeywordSelection
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QKeywordSelection extends EntityPathBase<KeywordSelection> {

    private static final long serialVersionUID = -370960837L;

    public static final QKeywordSelection keywordSelection = new QKeywordSelection("keywordSelection");

    public final ArrayPath<byte[], Byte> embedding = createArray("embedding", byte[].class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath keywords = createString("keywords");

    public final StringPath reason = createString("reason");

    public QKeywordSelection(String variable) {
        super(KeywordSelection.class, forVariable(variable));
    }

    public QKeywordSelection(Path<? extends KeywordSelection> path) {
        super(path.getType(), path.getMetadata());
    }

    public QKeywordSelection(PathMetadata metadata) {
        super(KeywordSelection.class, metadata);
    }

}

