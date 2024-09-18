package com.pretest.leesangyub.entity.pri;

import com.pretest.leesangyub.common.aop.InjectSequenceValue;
import com.pretest.leesangyub.constants.AppConstants;
import com.pretest.leesangyub.enums.RecruitField;
import com.pretest.leesangyub.utils.AppUtils;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;


@Slf4j
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "fl_profile") // 프리랜서 프로필 테이블
@Entity
public class Freelancer {

    @InjectSequenceValue(sequencename = "seq", tablename = "fl_profile")
    @Column(name = "seq", unique = true, nullable = false, updatable = false)
    @Setter
    public long seq;

    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fl_id", nullable = false, unique = true)
    private String flId = AppConstants.TEMPORARY_ID;  // 아이디

    @Column(name = "fl_name")
    private String flName; // 이름

    @Column(name = "expr_years")
    private Integer exprYears;  // 경력연수

    @Column(name = "recruit_field", nullable = false)
    @Enumerated(EnumType.STRING)
    private RecruitField recruitField;  // 지원분야

    @Column(name = "view_cnt", nullable = false)
    private Integer viewCnt;  // 조회수

    @Column(name = "accum_point", nullable = false)
    private Integer accumPoint;  // 프리랜서 누적포인트

    @Setter
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;  // 등록 시간

    @PrePersist
    public void onSave(){
        log.info("======================================= {}",flId);
        if(!AppUtils.hasValue(flId) || AppConstants.TEMPORARY_ID.equals(flId))
            flId = "fl_"+ String.format("%08d", seq);
    }

    @Override
    public String toString() {
        return "Freelancer{" +
                "seq=" + seq +
                ", flId='" + flId + '\'' +
                ", flName='" + flName + '\'' +
                ", exprYears=" + exprYears +
                ", recruitField=" + recruitField +
                ", viewCnt=" + viewCnt +
                ", accumPoint=" + accumPoint +
                ", createdAt=" + createdAt +
                '}';
    }
}
