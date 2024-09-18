package com.pretest.leesangyub.common.exception;

import com.pretest.leesangyub.common.data.CommonResponse;
import com.pretest.leesangyub.common.data.ErrorResponse;
import com.pretest.leesangyub.common.utils.CommonUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.ClientAbortException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RestControllerAdvice
public class ExceptionAdvice {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Hidden
    @GetMapping(value = "/error", consumes = {MediaType.ALL_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ErrorResponse<Void>> error(HttpServletRequest request) {
        int statusCode = HttpStatus.OK.value();
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        // int statusCode = status == null ? HttpStatus.OK.value() : Integer.parseInt(status.toString());
        if(status != null)
            statusCode = Integer.parseInt(status.toString());
        String message = switch (HttpStatus.valueOf(statusCode)) {
            case OK -> "ERROR";
            case NOT_FOUND -> "NOT FOUND";
            case INTERNAL_SERVER_ERROR -> "INTERNAL SERVER ERROR";
            default -> String.valueOf(request.getAttribute(RequestDispatcher.ERROR_MESSAGE));
        };
        return ResponseEntity.ok(ErrorResponse.of(statusCode, message));
    }

    @ExceptionHandler(ClientAbortException.class)
    protected void handleClientAbortException(ClientAbortException e) {
        log.info("{}", e.getMessage());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    protected ResponseEntity<CommonResponse<Void>> NoHandlerFoundExceptionException(NoHandlerFoundException e) {
        log.info("{}", e.getMessage());
        AppException appe = new AppException(null, AppCode.NO_HANDLER_FOUND, e.getMessage());
        return ResponseEntity.ok(CommonResponse.of(appe));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    protected ResponseEntity<CommonResponse<Void>> handleNoResourceFoundException(NoResourceFoundException e) {
        log.info("{}", e.getMessage());
        AppException appe = new AppException(null, AppCode.NO_RESOURCE_FOUND, e.getMessage());
        return ResponseEntity.ok(CommonResponse.of(appe));
    }

    /**
     * 지원하지 않은 HTTP method 호출 할 경우 발생
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<CommonResponse<Void>> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {

        log.info("{}", e.getMessage());
        AppException appe = new AppException(null, AppCode.HTTP_METHOD_NOT_SUPPORTED, e.getMessage());
        return ResponseEntity.ok(CommonResponse.of(appe));
    }

    /**
     * 지원하지 않은 HTTP MediaType 호출 할 경우 발생
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    protected ResponseEntity<CommonResponse<Void>> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e) {
        log.info("{}", e.getMessage());
        AppException appe = new AppException(null, AppCode.HTTP_MEDIA_TYPE_NOT_SUPPORTED, e.getMessage());
        return ResponseEntity.ok(CommonResponse.of(appe));
    }

    /**
     * Authentication 객체가 필요한 권한을 보유하지 않은 경우 발생
     */
    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<CommonResponse<Void>> handleAccessDeniedException(AccessDeniedException e) {
        AppException appe = new AppException(null, AppCode.ACCESS_NOTAUTHORISED);
        return ResponseEntity.ok(CommonResponse.of(appe));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<CommonResponse<Void>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {

        List<String> eparams = new ArrayList<>();
        List<FieldError> ferrs = e.getFieldErrors();
        ferrs.forEach(ferr -> {
            eparams.add(ferr.getField());
            log.info("{}", ferr.getField());
            log.info("{}", ferr.getDefaultMessage());
        });
        AppException appe = new AppException(null, AppCode.REQ_BODY_ERROR, eparams);
        CommonResponse<Void> commonResponse = CommonResponse.of(appe);
        log.debug("{}", CommonUtils.toJson(objectMapper, commonResponse));
        return ResponseEntity.ok(commonResponse);

    }

    @ExceptionHandler(UnrecognizedPropertyException.class)
    protected ResponseEntity<CommonResponse<Void>> handleUnrecognizedPropertyException(UnrecognizedPropertyException e) {
        List<String> eparams = new ArrayList<>();
        log.info("{}", e.getMessage());
        AppException appe = new AppException(null, AppCode.REQ_BODY_ERROR, eparams);
        CommonResponse<Void> commonResponse = CommonResponse.of(appe);
        log.debug("{}", CommonUtils.toJson(objectMapper, commonResponse));
        return ResponseEntity.ok(commonResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<CommonResponse<Void>> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        AppException appe = new AppException(null, AppCode.REQ_BODY_ERROR, e.getCause());
        CommonResponse<Void> commonResponse = CommonResponse.of(appe);
        log.debug("{}", CommonUtils.toJson(objectMapper, commonResponse));
        return ResponseEntity.ok(commonResponse);
    }

    /**
     * 예상하지 못한 에러
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse<Void>> handleException(Exception e) {
        log.info("", e);
        return ResponseEntity.ok(ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
    }

    /**
     * AppException을 throw하면 이곳에서 에러를 핸들링한다.
     */
    @ExceptionHandler(AppException.class)
    protected ResponseEntity<CommonResponse<Void>> handleAppException(AppException e) {
        final var code = e.getAppCode();

        CommonResponse<Void> commonResponse = CommonResponse.of(e);
        log.debug("{}", CommonUtils.toJson(objectMapper, commonResponse));
        return ResponseEntity.ok(commonResponse);

    }
}
