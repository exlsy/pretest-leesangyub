package com.pretest.leesangyub.common.data;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
@Builder
public class ErrorResponse<T> {
    private ErrorData error;
    private T data;

    public static <T> ErrorResponse<T> of(int statusCode, String message, T data) {
        final var statusText = statusCode + HttpStatus.valueOf(statusCode).getReasonPhrase();
        return new ErrorResponse<>( new ErrorData(statusText, message), data );
    }

    public static <T> ErrorResponse<T> of(int statusCode, String message) {
        return of( statusCode, message, null );
    }

}
