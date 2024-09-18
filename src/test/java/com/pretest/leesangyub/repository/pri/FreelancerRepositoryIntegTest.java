package com.pretest.leesangyub.repository.pri;

import com.pretest.leesangyub.constants.AppConstants;
import com.pretest.leesangyub.dto.freelancer.FreelancerSearchDTO;
import com.pretest.leesangyub.entity.pri.Freelancer;
import com.pretest.leesangyub.enums.RecruitField;
import com.pretest.leesangyub.utils.AppHelper;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import io.vavr.Tuple2;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@SpringBootTest
// @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)  // 매번 새로운 컨텍스트로 테스트 실행
public class FreelancerRepositoryIntegTest {

    @Autowired
    private FreelancerRepository freelancerRepository;

    private Freelancer freelancer;

    @BeforeEach
    public void setUp() {
        freelancer = Freelancer.builder()
                .flName("홍길동")
                .exprYears(5)
                .recruitField(RecruitField.BACKEND)
                .viewCnt(0)
                .accumPoint(0)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @AfterEach
    public void reset() {
        // Test이후 데이터 삭제
    }

    // CREATE Test
    @Test
    @Transactional
    public void testCreateFreelancer() {
        // When
        Freelancer savedFreelancer = freelancerRepository.save(freelancer);

        log.debug("-------------------- {}", savedFreelancer);

        // Then
        assertThat(savedFreelancer).isNotNull();
        assertThat(savedFreelancer.getSeq()).isNotNull();
        assertThat(savedFreelancer.getFlId()).isNotNull();
        assertThat(savedFreelancer.getFlName()).isEqualTo("홍길동");
        assertEquals(RecruitField.BACKEND, savedFreelancer.getRecruitField());
        assertEquals(0, savedFreelancer.getViewCnt());
    }

    // READ Test
    @Test
    @Transactional
    public void testFindFreelancerById() {
        // Given
        Freelancer savedFreelancer = freelancerRepository.save(freelancer);

        // When
        Optional<Freelancer> foundFreelancer = freelancerRepository.findFreelancerByFlId(savedFreelancer.getFlId());

        // Then
        assertThat(foundFreelancer.isPresent()).isTrue();
        assertThat(foundFreelancer.get().getFlName()).isEqualTo("홍길동");
        assertThat(foundFreelancer.get().getAccumPoint()).isEqualTo(0);
    }

    // READ Paging Test
    @Test
    @Transactional
    public void testFindPagedFreelancersByConditions() {
        // Given
        // Freelancer savedFreelancer = freelancerRepository.save(freelancer);
        PathBuilder<Freelancer> entity = new PathBuilder(Freelancer.class, "freelancer");

        // 쿼리조건 - 전체
        BooleanBuilder queryConds = AppHelper.composeFreelancerDynamicConditions( RecruitField.valueOf( "ALL"), "" );
        // 페이지조건
        PageRequest pageRequest = PageRequest.of(0, 4);
        // 소팅
        List<OrderSpecifier> orderSpecifiers = new ArrayList<>();
        orderSpecifiers.add( new OrderSpecifier(Order.ASC, entity.get("viewCnt")) );
        orderSpecifiers.add( new OrderSpecifier(Order.ASC, entity.get("createdAt")) );

        // When
        Tuple2<Long, Page<Freelancer>> pagedRes = freelancerRepository.getFreelancerPageByCondition(queryConds, pageRequest, orderSpecifiers);

        log.debug("-------------------- total count:{} data size:{}", pagedRes._1(), pagedRes._2().getSize());
        if(pagedRes._2().getSize() > 0) {
            pagedRes._2().stream().map( f -> {
                return FreelancerSearchDTO.FreelancerSearchRSB.builder()
                        .flId(f.getFlId())
                        .flName(f.getFlName())
                        .exprYears(f.getExprYears())
                        .recruitField(f.getRecruitField().getValue())
                        .viewCnt(f.getViewCnt())
                        .accumPoint(f.getAccumPoint())
                        .createdAt( f.getCreatedAt().format( DateTimeFormatter.ofPattern(AppConstants.DATE_BASIC_FMT) ) )
                        .build();
            } ).forEach(System.out::println);
        }

        // Then
        assertThat(pagedRes._1()).isEqualTo(11);
        assertThat(pagedRes._2().getSize()).isEqualTo(4);
    }

    // UPDATE viewCnt Test
    // Test 의 @Transactional 동작 확인 필요.
    @Test
    public void testUpdateFreelancerViewCntAndAccumPoint() {
        // Given
        Freelancer savedFreelancer = freelancerRepository.save(freelancer);
        int viewCnt = savedFreelancer.getViewCnt()+100;
        int addPoint = 5000;
        int accumPoint = savedFreelancer.getAccumPoint()+addPoint;
        log.debug("-------------------- {}", savedFreelancer);

        // When
        freelancerRepository.updateViewCntByFlId( savedFreelancer.getFlId(), viewCnt);
        freelancerRepository.addPointToAccumPointByFlId( savedFreelancer.getFlId(), 5000);
        Optional<Freelancer> foundFreelancer = freelancerRepository.findFreelancerByFlId(savedFreelancer.getFlId());
        log.debug("-------------------- {}", foundFreelancer.get());

        // Then
        assertThat(foundFreelancer.isPresent()).isTrue();
        assertThat(foundFreelancer.get().getViewCnt()).isEqualTo(viewCnt);
        assertThat(foundFreelancer.get().getAccumPoint()).isEqualTo(accumPoint);

        freelancerRepository.deleteFreelancerByFlId(savedFreelancer.getFlId());

    }



}
