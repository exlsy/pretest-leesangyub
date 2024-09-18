package com.pretest.leesangyub.component.apiclient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pretest.leesangyub.common.exception.AppCode;
import com.pretest.leesangyub.common.exception.AppException;
import com.pretest.leesangyub.common.utils.CommonUtils;
import com.pretest.leesangyub.dto.toss.PaymentConfirmDTO.ConfirmRQB;
//import com.pretest.leesangyub.dto.toss.PaymentConfirmDTO.ConfirmRSB;
import com.pretest.leesangyub.properties.PaymentProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
@RequiredArgsConstructor
@Component
public class TossClient {

    private final PaymentProperties paymentProperties;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public Object tossAPI_payConfirm(String trId, ConfirmRQB confirmRQB) {
        byte[] encodedBytes = Base64.getEncoder().encode((paymentProperties.toss().secretKey() + ":")
                .getBytes(StandardCharsets.UTF_8));

        final var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Basic " + new String(encodedBytes));

        final var apiUrl = paymentProperties.toss().confirmUrl();

        log.info("toss confirm apiUrl {}", apiUrl);
        log.info("toss confirm request {}", CommonUtils.toJson(objectMapper, confirmRQB));

        try {
            final var returnType = new ParameterizedTypeReference<Object>() {};
            final var resp = restTemplate.exchange(apiUrl, HttpMethod.POST, new HttpEntity<>(confirmRQB, headers), returnType);

            if (!resp.getStatusCode().is2xxSuccessful()) {
                log.info("is2xxSuccessful: {}", resp.getStatusCode().is2xxSuccessful());
                throw new AppException(trId, AppCode.PAYMENT_IF_ERROR, "toss 결제 실패");
            }
            final Object body = resp.getBody();

            log.debug("toss confirm response {}", CommonUtils.toJson(objectMapper, body));

            return resp.getBody() == null ? null : body;

        } catch (RestClientException e) {
            log.error("{}", apiUrl, e);
            // e.getMessage()
            // throw new AppException(trId, AppCode.PAYMENT_IF_ERROR, "toss 결제 REST 연동");
            throw new AppException(trId, AppCode.PAYMENT_IF_ERROR, e.getMessage());
        }
    }


}
