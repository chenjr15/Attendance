package dev.chenjr.attendance.exception;

public class SmsException extends RuntimeException {
    public SmsException() {
        super("SmsException");
    }

    public SmsException(String msg) {
        super(msg);
    }
}
