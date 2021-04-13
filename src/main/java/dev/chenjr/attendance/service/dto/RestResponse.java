package dev.chenjr.attendance.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.time.LocalDateTime;

public class RestResponse<T> implements Serializable {
    public static final int CODE_OK = 200;
    public static final int CODE_WRONG_ARGUMENT = 400;
    @Schema(description = "时间戳", example = "2021-04-13T16:09:30.8111435")
    public LocalDateTime timestamp;
    @Schema(description = "状态码", example = "200")
    public Integer status;
    @Schema(description = "请求的数据，依请求而定")
    @Nullable
    public T data;
    @Schema(description = "附加信息")
    public String message;


    public RestResponse() {
        this.timestamp = LocalDateTime.now();
    }


    public RestResponse(Integer status, String message, T data) {
        this();
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public RestResponse(Integer status, String message) {
        this();

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