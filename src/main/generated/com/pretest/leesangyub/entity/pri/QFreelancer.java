package com.pretest.leesangyub.entity.pri;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QFreelancer is a Querydsl query type for Freelancer
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFreelancer extends EntityPathBase<Freelancer> {

    private static final long serialVersionUID = -583613632L;

    public static final QFreelancer freelancer = new QFreelancer("freelancer");

    public final NumberPath<Integer> accumPoint = createNumber("accumPoint", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final NumberPath<Integer> exprYears = createNumber("exprYears", Integer.class);

    public final StringPath flId = createString("flId");

    public final StringPath flName = createString("flName");

    public final EnumPath<com.pretest.leesangyub.enums.RecruitField> recruitField = createEnum("recruitField", com.pretest.leesangyub.enums.RecruitField.class);

    public final NumberPath<Long> seq = createNumber("seq", Long.class);

    public final NumberPath<Integer> viewCnt = createNumber("viewCnt", Integer.class);

    public QFreelancer(String variable) {
        super(Freelancer.class, forVariable(variable));
    }

    public QFreelancer(Path<? extends Freelancer> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFreelancer(PathMetadata metadata) {
        super(Freelancer.class, metadata);
    }

}

