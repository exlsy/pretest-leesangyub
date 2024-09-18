package com.pretest.leesangyub.dto.freelancer;

import com.pretest.leesangyub.common.validator.ValidateString;
import com.pretest.leesangyub.dto.AppDTO.SortObj;
import com.pretest.leesangyub.dto.AppDTO.HeaderInfoObj;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Map;

public class FreelancerListDTO {

    /**
     * 프리랜서 조회 (리스트)
     */
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @JsonIgnoreProperties(ignoreUnknown =true)
    public static class FreelancerListRQB {
        @JsonProperty("isHeaderInfo")
        @Builder.Default
        private boolean isHeaderInfo = true;

        @NotNull(message = "rowCnt는 필수 값입니다.")
        @Builder.Default
        private int rowCnt = 20;

        @NotNull(message = "startNum은 필수 값입니다.")
        private int startNum;

        private List<SortObj> sortObjs;

        private String flName;

        @ValidateString(acceptedValues={"ALL","BACKEND","FRONTEND","CROSSAPP","MARKETING","DESIGN","PLANNING","PM"}, message="recruitField가 유효하지 않습니다.")
        private String recruitField;

        public Pageable getPageble() {
            if(rowCnt <=0) rowCnt = 20;
            int page = (int)(startNum / rowCnt);
            return PageRequest.of(page, rowCnt);
        }

    }


    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FreelancerListRSB {
        @NotEmpty(message = "totalCnt는 필수 값입니다.")
        private long totalCnt;

        private int curPage;

        @Nullable
        private List<HeaderInfoObj> headerInfos;

        private List<Map<String,Object>> freelancerList;

    }

}
