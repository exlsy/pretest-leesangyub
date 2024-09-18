package com.pretest.leesangyub.dto.toss;

import lombok.*;

public class PaymentConfirmDTO {
    @ToString
    @Getter
    @Builder
    @AllArgsConstructor
    @RequiredArgsConstructor
    public static class ConfirmRQB {
        private String orderId;
        private String amount;
        private String paymentKey;
    }

//    @Getter
//    @Builder
//    @AllArgsConstructor
//    @NoArgsConstructor
//    public class ConfirmRSB<T> {
//        private T body;
//    }
}
