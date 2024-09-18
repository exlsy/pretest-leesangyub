package com.pretest.leesangyub.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueryDslConfig {

    @PersistenceContext(unitName = "priEntityManager")
    private EntityManager priEntityManager;

    @Bean(name = "priJpaQueryFactory")
    public JPAQueryFactory priJpaQueryFactory() {
        return new JPAQueryFactory(priEntityManager);
    }

}
