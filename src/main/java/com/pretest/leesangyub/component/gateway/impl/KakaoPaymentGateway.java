package com.pretest.leesangyub.component.gateway.impl;

import com.pretest.leesangyub.component.gateway.PaymentGateway;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
// @ConditionalOnProperty(name = "taskapp.payment-gateway", havingValue = "kakao")
public class KakaoPaymentGateway implements PaymentGateway {

    @Override
    public Tuple2<Boolean, String> processPayment(String trId, String orderId, int amount, String paymentKey) {

        // 구현부 추가되어야 함.

        return Tuple.of(true, "결제 성공");
    }
}