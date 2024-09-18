package com.pretest.leesangyub.dto.point;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pretest.leesangyub.common.validator.ValidateString;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class PointChargeDTO {
    @Schema(description = "포인트 충전 요청")
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @JsonIgnoreProperties(ignoreUnknown =true)
    public static class PaymentPointRQB {

        @Schema(description = "프리랜서 ID", defaultValue = "fl_00000001")
        @NotNull(message = "flId는 필수 값입니다.")
        private String flId; // 프리랜서 ID

        @Schema(description = "결제 방법", defaultValue = "toss", allowableValues = {"toss", "kakao","paypal"})
        @ValidateString(acceptedValues={"toss","kakao","paypal"}, message="payType이 유효하지 않습니다.")
        private String payType;  // toss, kakao, paypal, ...

        @Schema(description = "주문번호", defaultValue = "MC45ODU2NDA0MDg5Nzk3")
        @NotNull(message = "orderId는 필수 값입니다.")
        private String orderId;  // 주문번호

        @Schema(description = "결제 금액(단위 KRW)",  defaultValue = "50000")
        @NotNull(message = "amount는 필수 값입니다.")
        private String amount; // 결제금액

        @Schema(description = "결제 키(paymentKey)",  defaultValue = "tgen_20240917001833r4sH5")
        @NotNull(message = "payKey는 필수 값입니다.")
        private String payKey; // 결제 키

        @Schema(description = "쿠폰 유무", defaultValue = "false")
        @JsonProperty("isCoupon")
        @Builder.Default
        private boolean isCoupon = false; // 쿠폰 유무

        @Schema(description = "쿠폰 번호",  defaultValue = "", allowableValues = {"CPN05", "CPN10","CPN15","CPN20"})
        private String couponNo; // 쿠폰 번호

        @Schema(description = "이벤트 유무", defaultValue = "false")
        @JsonProperty("isEvent")
        @Builder.Default
        private boolean isEvent = false;  // 이벤트 유무

        @Schema(description = "이벤트 번호",  defaultValue = "", allowableValues = {"EVT100", "EVT300","EVT500","EVT1000"})
        private String eventNo; // 이벤트 번호

    }

    @Schema(description = "포인트 충전 응답")
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PaymentPointRSB {

        // 결제응답
        private String payResult; // 결제 결과

        private String resultMessage; // 결과 메시지

        // 포인트 관련정보
        private String flId;  // 프리랜서 ID
        private String flName;   // 프리랜서 이름
        private int accumPoint;  // 누적포인트
        private String comment;  // 커멘트
    }
}
