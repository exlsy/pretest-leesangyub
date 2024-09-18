package com.pretest.leesangyub.component.gateway.impl;

import com.pretest.leesangyub.component.apiclient.TossClient;
import com.pretest.leesangyub.component.gateway.PaymentGateway;
import com.pretest.leesangyub.dto.toss.PaymentConfirmDTO.ConfirmRQB;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
// @ConditionalOnProperty(name = "taskapp.payment-gateway", havingValue = "toss")
public class TossPaymentGateway implements PaymentGateway {

    //
    private final TossClient tossClient;

    // 결제를 수행.
    @Override
    public Tuple2<Boolean, String> processPayment(String trId, String orderId, int amount, String paymentKey) {
        log.info("TossPaymentGateway.processPayment");

        ConfirmRQB confirmRQB = ConfirmRQB.builder()
                .orderId(orderId)
                .amount(""+amount)
                .paymentKey(paymentKey)
                .build();

        tossClient.tossAPI_payConfirm(trId, confirmRQB);

        return Tuple.of(true, "");

    }



}
