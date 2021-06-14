package dev.chenjr.attendance.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;


public class TokenException extends AuthenticationException {
    @Getter
    @Setter
    private HttpStatus status = HttpStatus.FORBIDDEN;

    public TokenException() {
        super("Token fail");
    }

    public TokenException(HttpStatus status) {
        super(status.name());
        this.status = status;
    }

    /**
     * Constructs an {@code AuthenticationException} with the specified message and root
     * cause.
     *
     * @param msg   the detail message
     * @param cause the root cause
     */
    public TokenException(String msg, Throwable cause) {
        super(msg, cause);
    }

    /**
     * Constructs an {@code AuthenticationException} with the specified message and no
     * root cause.
     *
     * @param msg the detail message
     */
    public TokenException(String msg) {
        super(msg);
    }

    public TokenException(HttpStatus sta, String message) {
        super(message);
        this.status = sta;
    }
}
