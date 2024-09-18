package com.pretest.leesangyub.service.freelancer;

import com.pretest.leesangyub.common.exception.AppCode;
import com.pretest.leesangyub.common.exception.AppException;
import com.pretest.leesangyub.constants.AppConstants;
import com.pretest.leesangyub.dto.AppDTO.HeaderInfoObj;
import com.pretest.leesangyub.dto.freelancer.FreelancerListDTO.FreelancerListRQB;
import com.pretest.leesangyub.dto.freelancer.FreelancerListDTO.FreelancerListRSB;
import com.pretest.leesangyub.dto.freelancer.FreelancerSearchDTO.FreelancerSearchRQB;
import com.pretest.leesangyub.dto.freelancer.FreelancerSearchDTO.FreelancerSearchRSB;
import com.pretest.leesangyub.entity.pri.Freelancer;
import com.pretest.leesangyub.enums.RecruitField;
import com.pretest.leesangyub.manager.FreelancerManager;
import com.pretest.leesangyub.properties.FreelancerProperties;
import com.pretest.leesangyub.repository.pri.FreelancerRepository;
import com.pretest.leesangyub.utils.AppHelper;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import io.vavr.Tuple2;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RequiredArgsConstructor
@Service
public class FreelancerServiceImpl implements FreelancerService {

    private final FreelancerManager freelancerManager;

    private final FreelancerProperties freelancerProperties;

    private final FreelancerRepository freelancerRepository;

    // 프리랜서 프로필 테이블 생성하기
    @Override
    @Transactional
    public synchronized String createFreelancerTable() {

        return freelancerManager.createTable(
                freelancerProperties.tableName(),
                (table) -> {
                    if ( !freelancerRepository.isTableExist(table) ) {
                        log.info("----- " + table + " 테이블이 없습니다. 테이블을 생성합니다. ");
                        if ( !freelancerRepository.makeTable(table)) {
                            log.error("***** "+table+" 테이블 생성에 실패했습니다.");
                            return "FAIL";
                        } else
                            return "SUCC";
                    } else {
                        log.info("----- " + table + " 테이블이 존재합니다. ");
                        return "EXIST";
                    }
                } );

    }

    // 프리랜서 프로필 테이블 카운트
    @Override
    public Long getFreelancerCount() {
        return freelancerRepository.count();
    }

    // 샘플 데이터 로딩하기
    @Override
    public void loadFreelancerSamples() {
        Object[][] freelancerObjs = {
                { "김소이", 3, "BACKEND", 0, 0 },
                { "이남훈", 5, "FRONTEND", 0, 3000 },
                { "홍길동", 10, "MARKETING", 30, 2000 },
                { "박수연", 2, "DESIGN", 5, 14000 },
                { "최대길", 18, "PM", 15, 134600 },
                { "이지수", 6, "BACKEND", 20, 1000 },
                { "조지훈", 4, "FRONTEND", 0, 0 },
                { "박나래", 14, "PLANNING", 52, 64000 },
                { "김준수", 1, "FRONTEND", 2, 3000 },
                { "장지현", 7, "CROSSAPP", 0, 1000 },
                { "정준수", 5, "BACKEND", 0, 6000 }
        };

        List<Freelancer> freelancers = Arrays.stream( freelancerObjs )
                .map( f ->
                     Freelancer.builder()
                        .flName( (String)f[0] )
                        .exprYears( (Integer)f[1] )
                        .recruitField( RecruitField.valueOf( (String)f[2] ) )
                        .viewCnt( (Integer)f[3])
                        .accumPoint( (Integer)f[4] )
                        .createdAt(LocalDateTime.now())
                        .build() )
                .toList();

        freelancers.forEach( freelancer -> {
            try { Thread.sleep(1000); } catch (InterruptedException e) { }
            freelancer.setCreatedAt(LocalDateTime.now());
            freelancerRepository.save( freelancer );
        });
    }


    // 프리랜서 리스트 조회 서비스
    @Override
    public FreelancerListRSB queryFreelancers(String trId, FreelancerListRQB listRQB) throws AppException {

        // 데이터 헤더 정보 구성
        Map<String, HeaderInfoObj> hdrInfoMap = AppHelper.freelancerHeaderInfoMap();
        final List<HeaderInfoObj> headerInfos = new ArrayList<HeaderInfoObj>();
        hdrInfoMap.forEach( (k,v) -> { headerInfos.add(v); } );

        // 동적 소팅 생성
        List<OrderSpecifier> orderSpecifiers = AppHelper.getFreelancerOrderSpecifiers(trId, hdrInfoMap, listRQB.getSortObjs());

        // 동적 쿼리 생성
        BooleanBuilder queryConds = AppHelper.composeFreelancerDynamicConditions(
                RecruitField.valueOf( listRQB.getRecruitField()),
                listRQB.getFlName() );

        StopWatch timer = new StopWatch();
        timer.start();

        Tuple2<Long, Page<Freelancer>> freelancers = freelancerRepository.getFreelancerPageByCondition(queryConds, listRQB.getPageble(), orderSpecifiers);

        timer.stop();
        log.info("Query tooks {} nanos ", (int)Math.round(timer.getTotalTimeNanos()) );

        return FreelancerListRSB.builder()
                .totalCnt( freelancers._1() )
                .curPage( listRQB.getPageble().getPageNumber() )
                .headerInfos( listRQB.isHeaderInfo() ? headerInfos : null )
                .freelancerList( composeBodyList(hdrInfoMap, freelancers._2().toList()) )
                .build();
    }

    private List<Map<String,Object>> composeBodyList(Map<String, HeaderInfoObj> hdrInfoMap, List<Freelancer> freelancers) {
        final List<Map<String,Object>> bodyList = new ArrayList<>();
        if(hdrInfoMap!=null && freelancers!=null) {
            AtomicInteger idx = new AtomicInteger(1);
            freelancers.forEach( freelancer -> {
                Map<String,Object> dataMap = new LinkedHashMap<>();
                dataMap.put("idx", idx.get() );
                hdrInfoMap.forEach( (k,v) -> {
                    switch(v.getKeyName()) {
                        case "fl_id" -> dataMap.put(v.getKeyName(), freelancer.getFlId());
                        case "fl_name" -> dataMap.put(v.getKeyName(), freelancer.getFlName());
                        case "expr_years" -> dataMap.put(v.getKeyName(), freelancer.getExprYears());
                        case "recruit_field" -> dataMap.put(v.getKeyName(), freelancer.getRecruitField());
                        case "view_cnt" -> dataMap.put(v.getKeyName(), freelancer.getViewCnt());
                        case "accum_point" -> dataMap.put(v.getKeyName(), freelancer.getAccumPoint());
                        case "created_at" -> dataMap.put(v.getKeyName(), freelancer.getCreatedAt()
                                .format( DateTimeFormatter.ofPattern(AppConstants.DATE_BASIC_FMT) ));
                        default -> {}
                    }
                });
                bodyList.add(dataMap);
                idx.getAndIncrement();
            } );
        }
        return bodyList;
    }

    // 프리랜서 조회 서비스 -> 조회수 업데이트
    @Override
    public FreelancerSearchRSB getFreelancer(String trId, FreelancerSearchRQB listRQB) throws AppException {

        FreelancerSearchRSB searchRSB = freelancerRepository.findFreelancerByFlId(listRQB.getFlId())
                .map( f -> FreelancerSearchRSB.builder()
                        .flId(f.getFlId())
                        .flName(f.getFlName())
                        .exprYears(f.getExprYears())
                        .recruitField(f.getRecruitField().getValue())
                        .viewCnt(f.getViewCnt())
                        .accumPoint(f.getAccumPoint())
                        .createdAt( f.getCreatedAt().format( DateTimeFormatter.ofPattern(AppConstants.DATE_BASIC_FMT) ) )
                        .build() )
                .orElseThrow( () -> new AppException(trId, AppCode.PROCESSING_1_ERROR, "프리랜서 "+listRQB.getFlId()+ " 없음.") ) ;

        log.info("searchRSB {} ", searchRSB );

        //
        freelancerManager.increaseViewCount(listRQB.getFlId());

        return searchRSB;
    }


}
