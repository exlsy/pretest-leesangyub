package com.pretest.leesangyub.repository.pri;

import com.pretest.leesangyub.entity.pri.Freelancer;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import io.vavr.Tuple2;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FreelancerDao {

    // 테이블 체크
    boolean isTableExist(String tableName);

    // 테이블 생성하기 (
    boolean makeTable(String tableName);

    // 프리랜서ID로 viewCnt 가져오기
    Integer getViewCntByFlId(String flId);

    // 프리랜서ID viewCnt 업데이트하기
    Long updateViewCntByFlId(String flId, int viewCnt);

    // 프리랜서ID accumPoint 업데이트하기
    Long addPointToAccumPointByFlId(String flId, int newPoint);

    // 프리랜서 조회 (페이징)
    Tuple2<Long, Page<Freelancer>> getFreelancerPageByCondition(Predicate queryConds, Pageable pageable, List<OrderSpecifier> orderSpecifiers);


}
