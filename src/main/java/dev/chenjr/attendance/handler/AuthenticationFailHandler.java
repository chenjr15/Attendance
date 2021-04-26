package dev.chenjr.attendance.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.chenjr.attendance.exception.TokenException;
import dev.chenjr.attendance.service.dto.RestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class AuthenticationFailHandler implements AuthenticationFailureHandler {
    @Autowired
    ObjectMapper mapper;

    /**
     * Called when an authentication attempt fails.
     *
     * @param request   the request during which the authentication attempt occurred.
     * @param response  the response.
     * @param exception the exception which was thrown to reject the authentication
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.error("INSIDE AuthenticationFailHandler");
        if (exception instanceof TokenException) {
            log.error("INSIDE TokenException");

            TokenException tokenException = (TokenException) exception;
            HttpStatus status = tokenException.getStatus();
            RestResponse<?> restResponse = RestResponse.error(status, tokenException.getMessage(), request.getRequestURI());
            String s = mapper.writeValueAsString(restResponse);
            response.getWriter().write(s);
            response.sendError(status.value());
        } else {
//            InsufficientAuthenticationException
            log.error("Non TokenException,{}", exception.getClass().getTypeName());
            response.sendError(HttpStatus.FORBIDDEN.value());
        }
    }
}
