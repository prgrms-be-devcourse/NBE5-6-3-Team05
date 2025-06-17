package com.grepp.moodlink.app.model.member.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = 738155327L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMember member = new QMember("member1");

    public final StringPath countries = createString("countries");

    public final DatePath<java.time.LocalDate> createdAt = createDate("createdAt", java.time.LocalDate.class);

    public final StringPath email = createString("email");

    public final StringPath genre = createString("genre");

    public final StringPath id = createString("id");

    public final com.grepp.moodlink.app.model.keyword.entity.QKeywordSelection keywordSelection;

    public final StringPath password = createString("password");

    public final StringPath periods = createString("periods");

    public final EnumPath<com.grepp.moodlink.app.model.auth.code.Role> role = createEnum("role", com.grepp.moodlink.app.model.auth.code.Role.class);

    public final DatePath<java.time.LocalDate> updatedAt = createDate("updatedAt", java.time.LocalDate.class);

    public final StringPath username = createString("username");

    public QMember(String variable) {
        this(Member.class, forVariable(variable), INITS);
    }

    public QMember(Path<? extends Member> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMember(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMember(PathMetadata metadata, PathInits inits) {
        this(Member.class, metadata, inits);
    }

    public QMember(Class<? extends Member> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.keywordSelection = inits.isInitialized("keywordSelection") ? new com.grepp.moodlink.app.model.keyword.entity.QKeywordSelection(forProperty("keywordSelection")) : null;
    }

}

