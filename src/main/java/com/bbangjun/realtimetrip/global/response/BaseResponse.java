package com.bbangjun.realtimetrip.global.response;


import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class BaseResponse<T> {

    private final Boolean success;
    private final Integer code;
    private final String message;
    private final T data;

    public BaseResponse(T data) {
        this.success = true;
        this.code = ResponseCode.OK.getCode();
        this.message = ResponseCode.OK.getMessage();
        this.data = data;
    }
    
    // 성공 시 null을 data로 받는 경우를 위한 추가 생성자
    public BaseResponse() {
        this.success = true;
        this.code = ResponseCode.OK.getCode();
        this.message = ResponseCode.OK.getMessage();
        this.data = null;
    }

    public BaseResponse(ResponseCode errorCode) {
        this.success = false;
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
        this.data = null;
    }

    public BaseResponse(ResponseCode errorCode, String errorMessage) {
        this.success = false;
        this.code = errorCode.getCode();
        this.message = errorMessage;
        this.data = null;
    }
}
