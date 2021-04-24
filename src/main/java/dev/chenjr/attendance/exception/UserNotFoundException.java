package dev.chenjr.attendance.exception;

public class UserNotFoundException extends SuperException {
    public UserNotFoundException() {
        super("UserNotFound");
    }

    public UserNotFoundException(String s) {
        super(s);
    }
}
