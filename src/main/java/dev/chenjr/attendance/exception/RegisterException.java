package dev.chenjr.attendance.exception;

public class RegisterException extends SuperException {
    public RegisterException() {
        super("RegisterException");
    }

    public RegisterException(String msg) {
        super(msg);
    }
}
