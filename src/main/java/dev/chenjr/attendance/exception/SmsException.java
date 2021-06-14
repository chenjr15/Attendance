package dev.chenjr.attendance.exception;

public class SmsException extends SuperException {
    public SmsException() {
        super("SmsException");
    }

    public SmsException(String msg) {
        super(msg);
    }
}
