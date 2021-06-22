package dev.chenjr.attendance.handler;

import dev.chenjr.attendance.exception.TokenException;
import dev.chenjr.attendance.service.dto.RestResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.TypeMismatchDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLIntegrityConstraintViolationException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @Operation(hidden = true)
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<RestResponse<?>> handleDAOException(DataAccessException daoEx, HttpServletRequest request) {
        RestResponse<?> error = new RestResponse<>();
        error.status = 400;
        Throwable rootCause = daoEx.getRootCause();
        String message = rootCause.getMessage();
        if (message == null) {
            message = "";
        }
        if (daoEx instanceof DuplicateKeyException) {

            String[] split = message.split("'");
            if (split.length > 1) {
                message = split[1];
            }
            error = RestResponse.error(HttpStatus.BAD_REQUEST, request, "关键字重复: " + message);
        } else if (daoEx instanceof TypeMismatchDataAccessException) {
            error = RestResponse.error(HttpStatus.BAD_REQUEST, request, "类型不匹配:" + message);
        }

        HttpStatus httpStatus = HttpStatus.resolve(error.status);
        if (httpStatus == null) {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(error, httpStatus);
    }

    @Operation(hidden = true)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public RestResponse<?> handleGlobalException(Exception ex, HttpServletRequest request) {
        log.error("Global Handler, {},{}", request.toString(), ex.getMessage());
        ex.printStackTrace();
        return RestResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, request, ex.getMessage());
    }

    @Operation(hidden = true)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public RestResponse<?> handleSQLException(SQLIntegrityConstraintViolationException ex, HttpServletRequest request) {
        log.error(request.toString(), ex.getMessage());
        ex.printStackTrace();
        return RestResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, request, "SQL ERROR!");
    }

    @Operation(hidden = true)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler({
            HttpRequestMethodNotSupportedException.class,
    })
    public RestResponse<?> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {

        log.error("{}:{},{}", ex.getClass().getTypeName(), request.toString(), ex.getMessage());
        return RestResponse.error(HttpStatus.METHOD_NOT_ALLOWED, request, ex.getMessage());
    }

    @Operation(hidden = true)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({
            TokenException.class,
    })
    public RestResponse<?> handleTokenException(TokenException ex, HttpServletRequest request) {

        log.error("{}:{},{}", ex.getClass().getTypeName(), request.toString(), ex.getMessage());
        return RestResponse.error(HttpStatus.UNAUTHORIZED, request, ex.getMessage());
    }


}
