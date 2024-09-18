package com.pretest.leesangyub.repository.pri.impl;

import com.pretest.leesangyub.entity.pri.Freelancer;
import com.pretest.leesangyub.repository.pri.FreelancerDao;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.pretest.leesangyub.entity.pri.QFreelancer.freelancer;

@Slf4j
@RequiredArgsConstructor
public class FreelancerDaoImpl implements FreelancerDao {

    @Autowired
    private EntityManager priEntityManager;

    @Autowired
    private JPAQueryFactory priJpaQueryFactory;

    // 테이블 체크
    @Override
    public boolean isTableExist(String tableName) {
        Query nativeQuery =
                priEntityManager.createNativeQuery("SELECT count(*) FROM information_schema.tables WHERE table_name = ?" )
                        .setParameter(1, tableName);
        long count = (Long) nativeQuery.getSingleResult();
        return count>0;
    }

    // 테이블 생성하기
    @Override
    public boolean makeTable(String tableName) {
        StringBuffer sbSQL = new StringBuffer();

        sbSQL.append(" CREATE TABLE IF NOT EXISTS `"+tableName+"` (                       \n");
        sbSQL.append(" `seq` int(11) NOT NULL COMMENT '일련번호 순번 : 1부터 시작한다',      \n");
        sbSQL.append(" `fl_id` varchar(32) NOT NULL COMMENT '프리랜서 식별자.   fl_ + 8자리 seq 의 형식 ex : fl_0000001',     \n");
        sbSQL.append(" `fl_name` varchar(64) NULL COMMENT '프리랜서 이름',      \n");
        sbSQL.append(" `expr_years` int(6) NULL COMMENT '경력연수',      \n");
        sbSQL.append(" `recruit_field` varchar(32) NOT NULL COMMENT '지원분야',      \n");
        sbSQL.append(" `view_cnt` int(11) NOT NULL DEFAULT 0 COMMENT '조회수',      \n");
        sbSQL.append(" `accum_point` int(11) NOT NULL DEFAULT 0 COMMENT '누적포인트',      \n");
        sbSQL.append(" `created_at` datetime NOT NULL COMMENT '등록시간',      \n");
        sbSQL.append(" PRIMARY KEY (`fl_id`),      \n");
        sbSQL.append(" KEY `idx_"+tableName+"_recruit_field` (`recruit_field`)       \n");
        sbSQL.append(" ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;       \n");

        log.info(sbSQL.toString());
        priEntityManager.createNativeQuery(sbSQL.toString()).executeUpdate();

        return isTableExist(tableName);

    }


    @Override
    public Integer getViewCntByFlId(String flId) {
        Integer viewCnt = priJpaQueryFactory.select(freelancer.viewCnt)
                .from(freelancer)
                .where(freelancer.flId.eq(flId))
                .fetchOne();
        if(viewCnt!=null)
            return viewCnt;
        return -1;
    }

    // 프리랜서ID viewCnt 업데이트하기
    @Override
    @Transactional
    public Long updateViewCntByFlId(String flId, int viewCnt) {
        long ucnt = priJpaQueryFactory
                .update( freelancer )
                .set( freelancer.viewCnt, viewCnt)
                .where( freelancer.flId.eq(flId))
                .execute();
        return ucnt;
    }

    // 프리랜서 accumPoint 업데이트하기
    @Override
    @Transactional
    public Long addPointToAccumPointByFlId(String flId, int newPoint) {
        long ucnt = priJpaQueryFactory
                .update( freelancer )
                .set( freelancer.accumPoint, freelancer.accumPoint.add( newPoint) )
                .where( freelancer.flId.eq(flId))
                .execute();
        return ucnt;
    }


    @Override
    public Tuple2<Long, Page<Freelancer>> getFreelancerPageByCondition(
            Predicate queryConds, Pageable pageable, List<OrderSpecifier> orderSpecifiers) {

        // total count 구하기
        JPAQuery<Long> countQuery = priJpaQueryFactory
                .select(freelancer.count())
                .from(freelancer)
                .where(queryConds)
                ;

        Long totalCount = countQuery.fetchOne();

        // total count와 페이징 처리한 결과를 tuple로 리턴.
        return Tuple.of(
                totalCount, new PageImpl<> (
                        priJpaQueryFactory
                                .selectFrom(freelancer)
                                .where(queryConds)
                                .orderBy(orderSpecifiers.toArray(new OrderSpecifier[orderSpecifiers.size()]))
                                .offset(pageable.getOffset())
                                .limit(pageable.getPageSize())
                                .fetch(),
                        pageable, totalCount ));
    }

}
