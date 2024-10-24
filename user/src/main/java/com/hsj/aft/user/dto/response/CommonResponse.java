package com.hsj.aft.user.dto.response;

import lombok.Getter;
import static com.hsj.aft.user.common.Constants.SUCCESS_CODE;
import static com.hsj.aft.user.common.Constants.SUCCESS_MESSAGE;

@Getter
public class CommonResponse {

    private final String code;
    private final String message;
    private final Object data;

    public CommonResponse() {
        this.code = SUCCESS_CODE;
        this.message = SUCCESS_MESSAGE;
        this.data = null;
    }

    public CommonResponse(String code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static CommonResponse success() {
        return new CommonResponse();  // 기본값 사용
    }

    public static CommonResponse success(Object data) {
        return new CommonResponse("200", "OK", data);
    }

    public static CommonResponse error(String code, String message) {
        return new CommonResponse(code, message, null);
    }
}