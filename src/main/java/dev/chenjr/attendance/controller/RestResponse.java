package dev.chenjr.attendance.controller;

import java.io.Serializable;

class RestResponse<T> implements Serializable {
    public static final int CODE_OK = 200;
    public static final int CODE_WRONG_ARGUMENT =400;
    public Integer code;
    public T data;
    public String msg;

    public RestResponse() {
    }

    public RestResponse(Integer code, String msg, T data) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

    public RestResponse(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public RestResponse(T data) {
        this(CODE_OK, "OK", data);
    }

    public static RestResponse<?> ok() {
        return new RestResponse<>();
    }

    public static <X> RestResponse<X> okWithData(X data) {
        return new RestResponse<>(data);
    }

    public static <X> RestResponse<X> okWithMsg(String msg) {
        return new RestResponse<>(CODE_OK, msg);
    }
}