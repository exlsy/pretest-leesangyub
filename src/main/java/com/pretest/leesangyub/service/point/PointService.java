package com.pretest.leesangyub.service.point;

import com.pretest.leesangyub.common.exception.AppException;
import com.pretest.leesangyub.dto.point.PointChargeDTO.PaymentPointRQB;
import com.pretest.leesangyub.dto.point.PointChargeDTO.PaymentPointRSB;

public interface PointService {

    // PG 결제를 통한 포인트 충전
    PaymentPointRSB chargePointWithPayment(String trId, PaymentPointRQB pointRQB) throws AppException;


}
