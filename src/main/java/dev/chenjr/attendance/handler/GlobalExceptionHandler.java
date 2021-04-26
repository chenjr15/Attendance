package dev.chenjr.attendance.handler;

import dev.chenjr.attendance.service.dto.RestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLIntegrityConstraintViolationException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public RestResponse<?> handleGlobalException(Exception ex, HttpServletRequest request) {
        log.error("Global Handler, {},{}", request.toString(), ex.getMessage());
        ex.printStackTrace();
        return RestResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request.getRequestURI());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public RestResponse<?> handleSQLException(SQLIntegrityConstraintViolationException ex, HttpServletRequest request) {
        log.error(request.toString(), ex.getMessage());
        ex.printStackTrace();
        return RestResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "SQL ERROR!", request.getRequestURI());
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler({
            HttpRequestMethodNotSupportedException.class,
    })
    public RestResponse<?> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {

        log.error(request.toString(), ex.getMessage());
        return RestResponse.error(HttpStatus.METHOD_NOT_ALLOWED, ex.getMessage(), request.getRequestURI());
    }

}
