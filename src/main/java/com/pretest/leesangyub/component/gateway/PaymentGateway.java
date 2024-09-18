package com.pretest.leesangyub.component.gateway;

import io.vavr.Tuple2;

public interface PaymentGateway {

    // pg를 통한 결제를 수행하고
    Tuple2<Boolean, String> processPayment(String trId, String orderId, int amount, String paymentKey);


}
