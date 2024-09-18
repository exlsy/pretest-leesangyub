package com.pretest.leesangyub.common.data;

import com.pretest.leesangyub.common.exception.AppCode;
import com.pretest.leesangyub.common.exception.AppException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommonResponse<T> {
    private CommonRespHeader header;
    private T body;

    public static <T> CommonResponse<T> ok(T data) {
        return new CommonResponse<>(CommonRespHeader.builder().trId("").rtnCode(AppCode.SUCCESS.getCode()).rtnMessage(AppCode.SUCCESS.getMessage()).build(), data);
    }

    public static <T> CommonResponse<T> ok(String trId, T data) {
        return new CommonResponse<>(CommonRespHeader.builder().trId(trId).rtnCode(AppCode.SUCCESS.getCode()).rtnMessage(AppCode.SUCCESS.getMessage()).build(), data);
    }

    public static <T> CommonResponse<T> nok(String trId, T data) {
        return new CommonResponse<>(CommonRespHeader.builder().trId(trId).rtnCode(AppCode.FAIL.getCode()).rtnMessage(AppCode.FAIL.getMessage()).build(), data);
    }

    public static <T> CommonResponse<T> ok(String trId) {
        return ok(trId, null);
    }


    public static <T> CommonResponse<T> of(AppException appe) {
        return of(null, null, appe);
    }

    public static <T> CommonResponse<T> of(String trId, AppException appe) {
        return of(null, trId, appe);
    }

    public static <T> CommonResponse<T> of(T data, String trId, AppException appe) {
        return new CommonResponse<>(CommonRespHeader.builder()
                .trId(trId!=null?trId:(appe.getTrId()!=null?appe.getTrId():""))
                .rtnCode(appe.getAppCode().getCode())
                .rtnMessage( appe.getMessage() ).build(), data);
    }

}
