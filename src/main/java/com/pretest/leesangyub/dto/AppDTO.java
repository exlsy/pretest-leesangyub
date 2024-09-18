package com.pretest.leesangyub.dto;

import com.pretest.leesangyub.common.validator.ValidateString;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class AppDTO {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class HeaderInfoObj {
        private int idx;
        private String keyName;
        private String name;
        private boolean isSort;
        private boolean isDisplay;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SortObj {
        private String field;

        @ValidateString(acceptedValues={"ASC","DESC"}, message="order가 유효하지 않습니다.")
        private String order;
    }

}
