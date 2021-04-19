package dev.chenjr.attendance.handler;

import dev.chenjr.attendance.exception.RegisterException;
import dev.chenjr.attendance.service.dto.RestResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class UserExceptionHandler {
    

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RegisterException.class)
    public RestResponse<String> handleRegisterExceptions(RegisterException ex) {
        return new RestResponse<>(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), null);
    }

}
