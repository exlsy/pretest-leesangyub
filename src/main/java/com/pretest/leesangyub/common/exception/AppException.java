package com.pretest.leesangyub.common.exception;

import lombok.Getter;

import java.text.MessageFormat;
import java.util.List;

@Getter
public class AppException extends RuntimeException {

    private final String trId;
    private final AppCode appCode;

    public AppException(AppCode appCode) {
        super(appCode.getMessage());
        this.trId = "";
        this.appCode = appCode;
    }
    public AppException(String trId, AppCode appCode) {
        super(appCode.getMessage());
        this.trId = trId;
        this.appCode = appCode;
    }

    public AppException(String trId, AppCode appCode, Object...params) {
        super(MessageFormat.format(appCode.getMessage().replace("'", "''"), params));
        this.trId = trId;
        this.appCode = appCode;
    }

    public AppException(String trId, AppCode appCode, List<String> params) {
        super(MessageFormat.format(appCode.getMessage().replace("'", "''"), params.toArray()));
        this.trId = trId;
        this.appCode = appCode;
    }

    public AppException(String trId, String message, AppCode appCode) {
        super(message);
        this.trId = trId;
        this.appCode = appCode;
    }

}
