package com.pretest.leesangyub.utils;

import com.pretest.leesangyub.common.exception.AppCode;
import com.pretest.leesangyub.common.exception.AppException;
import com.pretest.leesangyub.constants.AppConstants;
import com.pretest.leesangyub.dto.AppDTO.SortObj;
import com.pretest.leesangyub.dto.AppDTO.HeaderInfoObj;
import com.pretest.leesangyub.entity.pri.Freelancer;
import com.pretest.leesangyub.enums.RecruitField;
import com.google.common.base.CaseFormat;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.pretest.leesangyub.entity.pri.QFreelancer.freelancer;

public class AppHelper {

    public static Map<String, HeaderInfoObj> freelancerHeaderInfoMap() {

        Map<String, HeaderInfoObj> hdrInfoMap = new LinkedHashMap<>(); // 순서보장

        int idx = 1;
        hdrInfoMap.put("idx", HeaderInfoObj.builder().idx(idx++).keyName("idx").name("순번").isSort(false).isDisplay(true).build());
        hdrInfoMap.put("fl_id", HeaderInfoObj.builder().idx(idx++).keyName("fl_id").name("프리랜서 ID").isSort(false).isDisplay(false).build());
        hdrInfoMap.put("fl_name", HeaderInfoObj.builder().idx(idx++).keyName("fl_name").name("프리랜서 이름").isSort(true).isDisplay(true).build());
        hdrInfoMap.put("expr_years", HeaderInfoObj.builder().idx(idx++).keyName("expr_years").name("경력연수").isSort(true).isDisplay(true).build());
        hdrInfoMap.put("recruit_field", HeaderInfoObj.builder().idx(idx++).keyName("recruit_field").name("지원분야").isSort(false).isDisplay(true).build());
        hdrInfoMap.put("view_cnt", HeaderInfoObj.builder().idx(idx++).keyName("view_cnt").name("조회수").isSort(true).isDisplay(true).build());
        hdrInfoMap.put("accum_point", HeaderInfoObj.builder().idx(idx++).keyName("accum_point").name("누적포인트").isSort(false).isDisplay(false).build());
        hdrInfoMap.put("created_at", HeaderInfoObj.builder().idx(idx++).keyName("created_at").name("등록시간").isSort(true).isDisplay(true).build());

        return hdrInfoMap;

    }


    public static List<OrderSpecifier> getFreelancerOrderSpecifiers(String trId, Map<String, HeaderInfoObj> hdrInfoMap, List<SortObj> sorts) throws AppException {
        PathBuilder<Freelancer> entity = new PathBuilder(Freelancer.class, "freelancer");
        List<OrderSpecifier> orderSpecifiers = new ArrayList<>();
        if(!CollectionUtils.isEmpty(sorts )) {
            sorts.forEach(sort -> {
                if(!AppConstants.ORDER_DESC.equals(sort.getOrder()) && !AppConstants.ORDER_ASC.equals(sort.getOrder()) )
                    throw new AppException(trId, AppCode.REQ_BODY_ERROR, "sortObjs.order");

                final var hdrInfo = hdrInfoMap.get( sort.getField() );
                if(hdrInfo == null)
                    throw new AppException(trId, AppCode.REQ_BODY_ERROR, "sortObjs.field");

                if (hdrInfo.isSort()) {
                    String entityField = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, hdrInfo.getKeyName());
                    orderSpecifiers.add(new OrderSpecifier(AppConstants.ORDER_DESC.equals(sort.getOrder()) ? Order.DESC : Order.ASC, entity.get(entityField)));
                }
            });
        }
        if(orderSpecifiers.size() <= 0)
            orderSpecifiers.add( new OrderSpecifier( Order.DESC, entity.get("createdAt")) );

        return orderSpecifiers;
    }

    public static BooleanBuilder composeFreelancerDynamicConditions(RecruitField recruitField, String flName) {
        // 동적 쿼리 생성
        BooleanBuilder allConds = new BooleanBuilder();
        BooleanBuilder queryConds = new BooleanBuilder();

        if ( recruitField != RecruitField.ALL)
            allConds.and(freelancer.recruitField.eq( recruitField ));

        if ( AppUtils.hasValue(flName))
            queryConds.and( freelancer.flName.contains( flName ));

        return allConds.and(queryConds);
    }
}
