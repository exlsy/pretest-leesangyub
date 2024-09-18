package com.pretest.leesangyub.controller.point;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pretest.leesangyub.common.data.CommonReqHeader;
import com.pretest.leesangyub.common.data.CommonRequest;
import com.pretest.leesangyub.dto.freelancer.FreelancerListDTO;
import com.pretest.leesangyub.dto.point.PointChargeDTO.PaymentPointRQB;
import com.pretest.leesangyub.entity.pri.Freelancer;
import com.pretest.leesangyub.repository.pri.FreelancerRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
public class PointE2eTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private FreelancerRepository freelancerRepository;

    @Test
    @DisplayName("결제&포인트 적립 성공 테스트") // 페이징 결과
    public void testPaymentPoint_Success() throws Exception {

        // Given
        String pointTrId = "200000";
        String flId = "fl_00000001";

        PaymentPointRQB requestBody = PaymentPointRQB.builder()
                .flId(flId)
                .payType("kakao") // kakao는 결제성공으로 리턴.
                .orderId("MC42NTMzNDM4NTMzMjk0")     // 임의의 값
                .payKey("tgen_20240917001833r4sH5")  // 임의의 값
                .amount("10000")
                .isCoupon(false)
                .couponNo("")
                .isEvent(false)
                .eventNo("")
                .build();

        CommonRequest<PaymentPointRQB> request = CommonRequest.<PaymentPointRQB>builder()
                .header(CommonReqHeader.builder().trId(pointTrId).build())
                .body(requestBody)
                .build();

        // When & then
        // When
        Freelancer foundFreelancer = freelancerRepository.findFreelancerByFlId(flId).get();
        log.info("-------------------- foundFreelancer: {}", foundFreelancer);

        mockMvc.perform(post("/point/payment/charge")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)) )
                // .with(user("user").roles("USER"))) // 별도 인증정보 없음
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.header.rtnCode").value("000000"))
                .andExpect(jsonPath("$.body.payResult").value("SUCC"))
                .andExpect(jsonPath("$.body.accumPoint").value( foundFreelancer.getAccumPoint() + 10000 ))
                .andDo(print());

    }

    @Test
    @DisplayName("결제&포인트 적립이벤트 & 쿠폰 성공 테스트") // 페이징 결과
    public void testPaymentPointWithCouponAndEvent_Success() throws Exception {

        // Given
        String pointTrId = "200000";
        String flId = "fl_00000001";

        PaymentPointRQB requestBody = PaymentPointRQB.builder()
                .flId(flId)
                .payType("kakao") // kakao는 결제성공으로 리턴.
                .orderId("MC42NTMzNDM4NTMzMjk0")     // 임의의 값
                .payKey("tgen_20240917001833r4sH5")  // 임의의 값
                .amount("10000")
                .isCoupon(true)
                .couponNo("CPN10")
                .isEvent(true)
                .eventNo("EVT500")
                .build();

        CommonRequest<PaymentPointRQB> request = CommonRequest.<PaymentPointRQB>builder()
                .header(CommonReqHeader.builder().trId(pointTrId).build())
                .body(requestBody)
                .build();

        // 적립포인트 = 쿠폰할인 10% (10000 * 0.9) + 추가적립 500원 = 9500원
        int addPoint = 9500;

        // When & then
        // When
        Freelancer foundFreelancer = freelancerRepository.findFreelancerByFlId(flId).get();
        log.info("-------------------- foundFreelancer: {}", foundFreelancer);

        mockMvc.perform(post("/point/payment/charge")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)) )
                // .with(user("user").roles("USER"))) // 별도 인증정보 없음
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.header.rtnCode").value("000000"))
                .andExpect(jsonPath("$.body.payResult").value("SUCC"))
                .andExpect(jsonPath("$.body.accumPoint").value( foundFreelancer.getAccumPoint() + 9500 ))
                .andDo(print());

    }


    @Test
    @DisplayName("제공되지않는 payType으로 인한 Exception 테스트")
    public void testPaymentPoint_payTypeInvalid() throws Exception {
        // Given
        String pointTrId = "200000";
        String flId = "fl_00000001";

        PaymentPointRQB requestBody = PaymentPointRQB.builder()
                .flId(flId)
                .payType("naver") // naver는 현재 없음
                .orderId("MC42NTMzNDM4NTMzMjk0")     // 임의의 값
                .payKey("tgen_20240917001833r4sH5")  // 임의의 값
                .amount("10000")
                .isCoupon(false)
                .couponNo("")
                .isEvent(false)
                .eventNo("")
                .build();

        CommonRequest<PaymentPointRQB> request = CommonRequest.<PaymentPointRQB>builder()
                .header(CommonReqHeader.builder().trId(pointTrId).build())
                .body(requestBody)
                .build();

        // When & Then
        mockMvc.perform(post("/point/payment/charge")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)) )
                // .with(user("user").roles("USER"))) // 별도 인증정보 없음
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.header.rtnCode").value("E120003"))
                .andDo(print());

    }

    @Test
    @DisplayName("결제오류 인한 Exception 테스트")
    public void testPaymentPoint_notPayConfirmed() throws Exception {
        // Given
        String pointTrId = "200000";
        String flId = "fl_00000001";

        PaymentPointRQB requestBody = PaymentPointRQB.builder()
                .flId(flId)
                .payType("toss") //
                .orderId("MC42NTMzNDM4NTMzMjk0")     // 유효하지 않은 값
                .payKey("tgen_20240917001833r4sH5")  // 유효하지 않은 값
                .amount("10000")
                .isCoupon(false)
                .couponNo("")
                .isEvent(false)
                .eventNo("")
                .build();

        CommonRequest<PaymentPointRQB> request = CommonRequest.<PaymentPointRQB>builder()
                .header(CommonReqHeader.builder().trId(pointTrId).build())
                .body(requestBody)
                .build();

        // When & Then
        mockMvc.perform(post("/point/payment/charge")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)) )
                // .with(user("user").roles("USER"))) // 별도 인증정보 없음
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.header.rtnCode").value("E120101"))
                .andDo(print());

    }




}
