package dev.chenjr.attendance.exception;

public class SuperException extends RuntimeException {
    public SuperException() {
        super("Oops, something goes wrong...");
    }

    public SuperException(String msg) {
        super(msg);
    }
}
