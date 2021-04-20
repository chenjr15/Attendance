package dev.chenjr.attendance.handler;

import com.fasterxml.jackson.core.JsonParseException;
import dev.chenjr.attendance.exception.SuperException;
import dev.chenjr.attendance.service.dto.RestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestHandler {
    private static final String BAD_ARGUMENT_MESSAGE = "BAD_ARGUMENT_MESSAGE";
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public RestResponse<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<FieldError> fieldErrors = ex.getFieldErrors();
        TreeMap<String, String> errorMap = new TreeMap<>();
        fieldErrors.forEach(fieldError -> errorMap.put(fieldError.getField(), fieldError.getDefaultMessage()));
        return new RestResponse<>(HttpStatus.BAD_REQUEST.value(), BAD_ARGUMENT_MESSAGE, errorMap);
    }

    @ExceptionHandler({JsonParseException.class, SuperException.class})
    public RestResponse<?> handleJsonParseException(JsonParseException ex) {
        log.error(ex.getMessage());
        return new RestResponse<>(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    }

}
