package dev.chenjr.attendance.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.chenjr.attendance.exception.TokenException;
import dev.chenjr.attendance.service.dto.RestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class AuthenticationFailHandler implements AuthenticationEntryPoint {
    @Autowired
    ObjectMapper mapper;

    /**
     * Commences an authentication scheme.
     * <p>
     * <code>ExceptionTranslationFilter</code> will populate the <code>HttpSession</code>
     * attribute named
     * <code>AbstractAuthenticationProcessingFilter.SPRING_SECURITY_SAVED_REQUEST_KEY</code>
     * with the requested target URL before calling this method.
     * <p>
     * Implementations should modify the headers on the <code>ServletResponse</code> as
     * necessary to commence the authentication process.
     *
     * @param request   that resulted in an <code>AuthenticationException</code>
     * @param response  so that the user agent can begin authentication
     * @param exception that caused the invocation
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        log.error("INSIDE AuthenticationEntryPoint,{}", request.getRequestURI());
        if (exception instanceof TokenException) {
            log.error("INSIDE TokenException");

            TokenException tokenException = (TokenException) exception;
            HttpStatus status = tokenException.getStatus();
            RestResponse<?> restResponse = RestResponse.error(status, request, tokenException.getMessage());
            String s = mapper.writeValueAsString(restResponse);
//            response.getWriter().write(s);
            response.sendError(status.value(), s);
        } else {
//            InsufficientAuthenticationException
            log.error("Non TokenException,{}", exception.getClass().getTypeName());
            response.sendError(HttpStatus.FORBIDDEN.value());
        }
    }
}
