package com.pretest.leesangyub.controller.point;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pretest.leesangyub.common.SessionInfo;
import com.pretest.leesangyub.common.data.CommonRequest;
import com.pretest.leesangyub.common.data.CommonResponse;
import com.pretest.leesangyub.common.exception.AppCode;
import com.pretest.leesangyub.common.exception.AppException;
import com.pretest.leesangyub.common.utils.CommonUtils;
import com.pretest.leesangyub.dto.point.PointChargeDTO.PaymentPointRQB;
import com.pretest.leesangyub.dto.point.PointChargeDTO.PaymentPointRSB;
import com.pretest.leesangyub.service.point.PointService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="포인트 서비스", description = "포인트 서비스 API")
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/point")
@RestController
public class PointController {

    private final PointService paymentService;

    private final ObjectMapper objectMapper;

    @Operation(summary = "포인트 충전", description = "포인트 - 결제를 통한 포인트 충전 서비스 API")
    @PostMapping("/payment/charge")
    public CommonResponse<PaymentPointRSB> chargePointWithPayment(
            @AuthenticationPrincipal SessionInfo sessionInfo,
            @Valid @RequestBody CommonRequest<PaymentPointRQB> payPointRequest ) {

        final var pointTrId = "200000";

        log.debug("{} {}", sessionInfo, CommonUtils.toJson(objectMapper, payPointRequest));

        if(!pointTrId.equals( payPointRequest.getHeader().getTrId()) )
            throw new AppException(pointTrId, AppCode.TRID_INVALID, payPointRequest.getHeader().getTrId());

        CommonResponse<PaymentPointRSB> payPointResponse =  CommonResponse.ok(pointTrId,
                paymentService.chargePointWithPayment(
                        pointTrId,
                        payPointRequest.getBody()));

        log.debug("{}", CommonUtils.toJson(objectMapper, payPointResponse));

        return payPointResponse;

    }

}
