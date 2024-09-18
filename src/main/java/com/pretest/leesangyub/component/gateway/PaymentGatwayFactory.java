package com.pretest.leesangyub.component.gateway;

import com.pretest.leesangyub.component.gateway.impl.KakaoPaymentGateway;
import com.pretest.leesangyub.component.gateway.impl.TossPaymentGateway;
import com.pretest.leesangyub.properties.PaymentProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class PaymentGatwayFactory {

    private final PaymentProperties paymentProperties;

    private final TossPaymentGateway tossPaymentGateway;
    private final KakaoPaymentGateway kakaoPaymentGateway;


    public PaymentGateway getPaymentGateway(String pgName) {
        switch (pgName) {
            case "toss" -> { return tossPaymentGateway; }
            case "kakao" -> { return kakaoPaymentGateway; }
            // 추가...
        }
        return kakaoPaymentGateway;
    }

}
