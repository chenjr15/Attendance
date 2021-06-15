package dev.chenjr.attendance.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class HttpStatusException extends SuperException {
    @Getter
    HttpStatus status;

    public HttpStatusException(HttpStatus status) {
        super(status.name());
        this.status = status;
    }

    public HttpStatusException(HttpStatus status, String msg) {
        super(msg);
        this.status = status;
    }

    public static HttpStatusException notFound() {
        return new HttpStatusException(HttpStatus.NOT_FOUND);
    }

    public static HttpStatusException notFound(String msg) {
        return new HttpStatusException(HttpStatus.NOT_FOUND, msg);
    }

    public static HttpStatusException badRequest(String msg) {
        return new HttpStatusException(HttpStatus.BAD_REQUEST, msg);
    }

}
