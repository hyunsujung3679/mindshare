package com.hsj.aft.common.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static com.hsj.aft.common.constants.Constants.SUCCESS_CODE;

@Getter
public class CommonResponse {

    private final String code;
    private final Object message;
    private final Object data;

    public CommonResponse() {
        this.code = SUCCESS_CODE;
        this.message = HttpStatus.OK.getReasonPhrase();
        this.data = null;
    }

    public CommonResponse(String code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public CommonResponse(String code, Object message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static CommonResponse success() {
        return new CommonResponse();
    }

    public static CommonResponse success(Object data) {
        return new CommonResponse(String.valueOf(HttpStatus.OK.value()), "OK", data);
    }

    public static CommonResponse error(String code, String message) {
        return new CommonResponse(code, message, null);
    }

    public static CommonResponse error(String code, Object message) {
        return new CommonResponse(code, message, null);
    }
}