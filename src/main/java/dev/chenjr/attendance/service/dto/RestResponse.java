package dev.chenjr.attendance.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.time.LocalDateTime;

public class RestResponse<T> implements Serializable {
    public static final int CODE_OK = 200;
    public static final int CODE_WRONG_ARGUMENT = 400;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "时间戳", example = "2021-04-13 16:09:30")
    public LocalDateTime timestamp;
    @Schema(description = "状态码")
    public Integer status;
    @Schema(description = "请求的数据，依请求而定")
    public T data;
    @Schema(description = "附加信息")
    public String message;
    // JsonInclude.Include.NON_NULL : 仅在非空的时候才输出这个字段
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String method;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String path;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String error;

    public RestResponse() {
        this.timestamp = LocalDateTime.now();
    }


    public RestResponse(Integer status, String message, T data) {
        this();
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public RestResponse(Integer status, String message, String path, String error) {
        this();
        this.status = status;
        this.path = path;
        this.error = error;
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

    public static RestResponse<?> httpStatus(HttpStatus status) {
        return new RestResponse<>(status.value(), status.name());
    }

    public static RestResponse<?> badRequest(String msg, Object data) {
        return new RestResponse<>(HttpStatus.BAD_REQUEST.value(), msg, data);
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

    // 错误返回
    public static RestResponse<?> error(HttpStatus status, String msg, String path) {
        return new RestResponse<>(status.value(), msg, path, status.name());
    }

    public static RestResponse<?> error(HttpStatus status, HttpServletRequest request, String msg) {
        RestResponse<?> objectRestResponse = new RestResponse<>(
                status.value(), msg, request.getRequestURI(), status.name());
        objectRestResponse.method = request.getMethod();
        return objectRestResponse;
    }

    public static <X> RestResponse<X> errorWithData(HttpStatus status, HttpServletRequest request, String msg, X data) {
        RestResponse<X> objectRestResponse = new RestResponse<>(
                status.value(), msg, request.getRequestURI(), status.name());
        objectRestResponse.method = request.getMethod();
        objectRestResponse.data = data;
        return objectRestResponse;
    }

    public static <X> RestResponse<X> error(HttpStatus status, String msg, String path, X data) {
        RestResponse<X> response = new RestResponse<>(status.value(), msg, path, status.name());
        response.data = data;
        return response;
    }

    public static RestResponse<?> error(Integer status, String msg, String path, String error) {
        return new RestResponse<>(status, msg, path, error);
    }

    public static RestResponse<?> badRequest(String msg, String path, String error) {
        return new RestResponse<>(HttpStatus.BAD_REQUEST.value(), msg, path, error);
    }

}