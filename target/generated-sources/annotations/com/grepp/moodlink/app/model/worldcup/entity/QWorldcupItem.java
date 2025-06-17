package com.grepp.moodlink.app.model.worldcup.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QWorldcupItem is a Querydsl query type for WorldcupItem
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QWorldcupItem extends EntityPathBase<WorldcupItem> {

    private static final long serialVersionUID = -2077279786L;

    public static final QWorldcupItem worldcupItem = new QWorldcupItem("worldcupItem");

    public final StringPath contentId = createString("contentId");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> totalCount = createNumber("totalCount", Long.class);

    public final NumberPath<Long> winCount = createNumber("winCount", Long.class);

    public final NumberPath<Long> worldcupId = createNumber("worldcupId", Long.class);

    public QWorldcupItem(String variable) {
        super(WorldcupItem.class, forVariable(variable));
    }

    public QWorldcupItem(Path<? extends WorldcupItem> path) {
        super(path.getType(), path.getMetadata());
    }

    public QWorldcupItem(PathMetadata metadata) {
        super(WorldcupItem.class, metadata);
    }

}

