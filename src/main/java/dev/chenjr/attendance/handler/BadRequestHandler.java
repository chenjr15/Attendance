package dev.chenjr.attendance.handler;

import com.fasterxml.jackson.core.JsonParseException;
import dev.chenjr.attendance.exception.SuperException;
import dev.chenjr.attendance.service.dto.RestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestHandler {
    private static final String BAD_ARGUMENT_MESSAGE = "BAD_ARGUMENT_MESSAGE";

    /**
     * TODO extends ResponseEntityExceptionHandler
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public RestResponse<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<FieldError> fieldErrors = ex.getFieldErrors();
        TreeMap<String, String> errorMap = new TreeMap<>();
        fieldErrors.forEach(fieldError -> errorMap.put(fieldError.getField(), fieldError.getDefaultMessage()));
        RestResponse<Map<String, String>> response =
                new RestResponse<>(HttpStatus.BAD_REQUEST.value(), BAD_ARGUMENT_MESSAGE, errorMap);
        response.path = request.getRequestURI();
        response.error = HttpStatus.BAD_REQUEST.name();
//        ResponseEntityExceptionHandler
        return response;
    }


    @ExceptionHandler({
            JsonParseException.class,
            SuperException.class,
            MethodArgumentTypeMismatchException.class,
            HttpRequestMethodNotSupportedException.class,
            AuthenticationException.class
    })
    public RestResponse<?> handleManyException(Exception ex, HttpServletRequest request) {

        log.error(request.toString(), ex.getMessage());
        return RestResponse.error(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI());
    }


}
