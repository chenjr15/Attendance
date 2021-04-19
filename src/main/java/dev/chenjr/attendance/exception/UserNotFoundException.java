package dev.chenjr.attendance.exception;

public class UserNotFoundException extends SuperException {
    public UserNotFoundException() {
        super("UserNotFoundException");
    }

    public UserNotFoundException(String s) {
        super(s);
    }
}
