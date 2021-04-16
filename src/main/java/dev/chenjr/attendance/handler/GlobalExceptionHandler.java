package dev.chenjr.attendance.handler;

import dev.chenjr.attendance.service.dto.RestResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public RestResponse<?> handleGlobalException(Exception ex) {

        return new RestResponse<>(HttpStatus.BAD_REQUEST.value(), HttpStatus.INTERNAL_SERVER_ERROR.name());
    }
}
