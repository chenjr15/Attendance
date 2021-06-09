package dev.chenjr.attendance.handler;

import com.fasterxml.jackson.core.JsonParseException;
import dev.chenjr.attendance.exception.HttpStatusException;
import dev.chenjr.attendance.exception.SuperException;
import dev.chenjr.attendance.service.dto.RestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestHandler {
    private static final String BAD_ARGUMENT_MESSAGE = "BAD_ARGUMENT";

    /**
     * TODO extends  ResponseEntityExceptionHandler
     * see: org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public RestResponse<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<FieldError> fieldErrors = ex.getFieldErrors();
        TreeMap<String, String> errorMap = new TreeMap<>();
        fieldErrors.forEach(fieldError -> errorMap.put(fieldError.getField(), fieldError.getDefaultMessage()));

        return RestResponse.error(HttpStatus.BAD_REQUEST, BAD_ARGUMENT_MESSAGE, request.getRequestURI(), errorMap);
    }

    @ExceptionHandler(HttpStatusException.class)
    public ResponseEntity<Map<String, Object>> handleHttpStatusException(HttpStatusException ex, HttpServletRequest request) {

        Map<String, Object> map = new TreeMap<>();
        map.put("timestamp", LocalDateTime.now());
        map.put("status", ex.getStatus().value());
        map.put("message", ex.getStatus().name());
        map.put("path", request.getRequestURI());
        return new ResponseEntity<>(map, ex.getStatus());

    }

    @ExceptionHandler({
            JsonParseException.class,
            SuperException.class,
            MethodArgumentTypeMismatchException.class,
            AuthenticationException.class
    })
    public RestResponse<?> handleManyException(Exception ex, HttpServletRequest request) {

        log.error("handleManyException:{},{}", request.toString(), ex.getMessage());
        RestResponse<?> error = RestResponse.error(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI());
        if (ex instanceof JsonParseException) {
            error.message = "Json 格式化错误";
        }
        return error;
    }


}
