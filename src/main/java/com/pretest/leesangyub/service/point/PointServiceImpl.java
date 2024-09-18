package com.pretest.leesangyub.service.point;

import com.pretest.leesangyub.common.exception.AppCode;
import com.pretest.leesangyub.common.exception.AppException;
import com.pretest.leesangyub.component.gateway.PaymentGateway;
import com.pretest.leesangyub.component.gateway.PaymentGatwayFactory;
import com.pretest.leesangyub.constants.AppConstants;
import com.pretest.leesangyub.dto.freelancer.FreelancerSearchDTO;
import com.pretest.leesangyub.dto.point.PointChargeDTO.PaymentPointRQB;
import com.pretest.leesangyub.dto.point.PointChargeDTO.PaymentPointRSB;
import com.pretest.leesangyub.entity.pri.Freelancer;
import com.pretest.leesangyub.repository.pri.FreelancerRepository;
import io.vavr.Tuple2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
@RequiredArgsConstructor
@Service
public class PointServiceImpl implements PointService {

    private final PaymentGatwayFactory paymentGatwayFactory;

    private final FreelancerRepository freelancerRepository;

    // 임시 -- 쿠폰 Map
    private final Map<String, Double> couponMap = Map.of(
            "CPN05", 0.95,
            "CPN10", 0.9,
            "CPN15", 0.85,
            "CPN20", 0.8
    );

    // 임시 -- 추가 적립포인트 Map
    private final Map<String, Integer> eventMap = Map.of(
            "EVT100", 100,
            "EVT300", 300,
            "EVT500", 500,
            "EVT1000", 1000
    );

    // PG 결제를 통한 포인트 충전
    @Override
    public PaymentPointRSB chargePointWithPayment(String trId, PaymentPointRQB pointRQB) throws AppException {

        freelancerRepository.findFreelancerByFlId(pointRQB.getFlId())
                .orElseThrow( () -> new AppException(trId, AppCode.PROCESSING_1_ERROR, "해당 프리랜서 "+pointRQB.getFlId()+ " 없음.") ) ;

        final var orderId = pointRQB.getOrderId();
        final var orgPrice = Integer.parseInt( pointRQB.getAmount() );
        final var paymentKey = pointRQB.getPayKey();

        int amount = orgPrice;
        int addPoint = 0;

        // 할인쿠폰 적용 -- 임시코드임
        if(pointRQB.isCoupon() && !couponMap.containsKey(pointRQB.getCouponNo()))
            throw new AppException(trId, AppCode.POINT_REQ_INVALID, "할인쿠폰번호");
        if(pointRQB.isCoupon() && couponMap.containsKey(pointRQB.getCouponNo())) {
            amount = (int) (orgPrice * couponMap.get(pointRQB.getCouponNo()));
            log.info("할인쿠폰 적용 원가격:{} 할인가격:{}", orgPrice, amount);
        }

        // 이벤트 적용 (추가적립) -- 임시코드임
        if(pointRQB.isEvent() && !eventMap.containsKey(pointRQB.getEventNo()))
            throw new AppException(trId, AppCode.POINT_REQ_INVALID, "적립이벤트번호");
        if(pointRQB.isEvent() && eventMap.containsKey(pointRQB.getEventNo())) {
            addPoint = eventMap.get(pointRQB.getEventNo());
            log.info("추가 적립 포인트:{} ", addPoint);
        }

        // PG를 통한 결제처리
        PaymentGateway pg = paymentGatwayFactory.getPaymentGateway(pointRQB.getPayType());

        Tuple2<Boolean, String> payResult = pg.processPayment(trId, orderId, amount, paymentKey);

        // 프리랜서에 누적포인트(accum_point) 적립하기
        if(payResult._1()) {
            log.info("원가격 {} 할인가격 {} 적립포인트 {}", orgPrice, amount, amount+addPoint);
            freelancerRepository.addPointToAccumPointByFlId(pointRQB.getFlId(), amount+addPoint);
        }

        Freelancer findFreelancer = freelancerRepository.findFreelancerByFlId(pointRQB.getFlId()).get();

        // 포인트 처리 응답
        PaymentPointRSB pointRSB = PaymentPointRSB.builder()
                .payResult(payResult._1()?AppConstants.SUCC:AppConstants.FAIL)
                .resultMessage(payResult._2())
                .flId(findFreelancer.getFlId())
                .flName(findFreelancer.getFlName())
                .accumPoint(findFreelancer.getAccumPoint())
                .comment( payResult._1()?"원가격:"+orgPrice+" -> 할인가격:"+amount+" 추가적립포인트:"+addPoint
                        +" --> 적립포인트:"+(amount+addPoint)+" 누적포인트:"+findFreelancer.getAccumPoint() :"" )
                .build();

        return pointRSB;

    }


}
