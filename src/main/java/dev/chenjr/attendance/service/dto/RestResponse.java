package dev.chenjr.attendance.service.dto;

import org.springframework.http.HttpStatus;

import java.io.Serializable;

public class RestResponse<T> implements Serializable {
    public static final int CODE_OK = 200;
    public static final int CODE_WRONG_ARGUMENT = 400;
    public Integer status;
    public T data;
    public String message;

    public RestResponse() {
    }

    public RestResponse(Integer status, String message, T data) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public RestResponse(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    public RestResponse(T data) {
        this(CODE_OK, "OK", data);
    }

    public static RestResponse<?> ok() {
        return new RestResponse<>(CODE_OK, "OK");
    }

    public static RestResponse<?> notImplemented() {
        return new RestResponse<>(HttpStatus.NOT_IMPLEMENTED.value(), "还没有实现！");
    }

    public static <X> RestResponse<X> okWithData(X data) {
        return new RestResponse<>(data);
    }

    public static <X> RestResponse<X> okWithMsg(String msg) {
        return new RestResponse<>(CODE_OK, msg);
    }
}