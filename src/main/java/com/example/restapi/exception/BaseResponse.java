package com.example.restapi.exception;

import lombok.Getter;

@Getter
public class BaseResponse {
    private Integer status;
    private String message;

    private Object result;

    public BaseResponse(ErrorCode errorCode) {
        this.status = errorCode.getStatus().value();
        this.message = errorCode.getMessage();
    }

    public BaseResponse(ErrorCode errorCode, Object object) {
        this.status = errorCode.getStatus().value();
        this.message = errorCode.getMessage();
        this.result = object;
    }
}
