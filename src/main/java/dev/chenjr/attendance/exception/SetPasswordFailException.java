package dev.chenjr.attendance.exception;

public class SetPasswordFailException extends SuperException {

    public static final String MESSAGE = "Set password fail.";

    public SetPasswordFailException() {
        super(MESSAGE);
    }
}
