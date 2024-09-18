package com.pretest.leesangyub.dto.freelancer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class FreelancerSearchDTO {
    /**
     * 프리랜서 조회
     */
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @JsonIgnoreProperties(ignoreUnknown =true)
    public static class FreelancerSearchRQB {

        @Schema(description = "프리랜서 ID", defaultValue = "fl_00000001")
        @NotNull(message = "flId는 필수 값입니다.")
        private String flId;

    }


    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FreelancerSearchRSB {

        @Schema(description = "프리랜서 ID")
        private String flId;

        @Schema(description = "프리랜서 이름")
        private String flName;

        @Schema(description = "경력연수")
        private int exprYears;

        @Schema(description = "지원분야")
        private String recruitField;

        @Schema(description = "조회수")
        private int viewCnt;

        @Schema(description = "누적포인트")
        private int accumPoint;

        @Schema(description = "등록일자")
        private String createdAt;

    }
}
