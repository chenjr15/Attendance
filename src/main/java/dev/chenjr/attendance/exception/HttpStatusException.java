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
}
