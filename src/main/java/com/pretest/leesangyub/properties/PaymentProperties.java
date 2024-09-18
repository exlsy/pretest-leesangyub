package com.pretest.leesangyub.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "taskapp.payment-gateway")
public record PaymentProperties(
        String defaultPg,
        TossProperties toss
) {
    public record TossProperties(
            String secretKey,
            String confirmUrl
    ) {
    }
}
