package dev.chenjr.attendance.exception;

public class AccountExistsException extends SuperException {
    public AccountExistsException() {
        super("Account already exists.");
    }

    public AccountExistsException(String s) {
        super(s);
    }
}
