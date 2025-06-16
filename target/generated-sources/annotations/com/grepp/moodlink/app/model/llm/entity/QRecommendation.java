package com.grepp.moodlink.app.model.llm.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QRecommendation is a Querydsl query type for Recommendation
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRecommendation extends EntityPathBase<Recommendation> {

    private static final long serialVersionUID = -481053355L;

    public static final QRecommendation recommendation = new QRecommendation("recommendation");

    public final com.grepp.moodlink.infra.entity.QBaseEntity _super = new com.grepp.moodlink.infra.entity.QBaseEntity(this);

    //inherited
    public final BooleanPath activated = _super.activated;

    public final StringPath contentId = createString("contentId");

    public final StringPath contentType = createString("contentType");

    //inherited
    public final DatePath<java.time.LocalDate> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath keywords = createString("keywords");

    //inherited
    public final DatePath<java.time.LocalDate> modifiedAt = _super.modifiedAt;

    public final StringPath reason = createString("reason");

    public QRecommendation(String variable) {
        super(Recommendation.class, forVariable(variable));
    }

    public QRecommendation(Path<? extends Recommendation> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRecommendation(PathMetadata metadata) {
        super(Recommendation.class, metadata);
    }

}

