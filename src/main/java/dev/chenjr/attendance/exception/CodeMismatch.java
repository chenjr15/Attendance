package dev.chenjr.attendance.exception;

public class CodeMismatch extends SmsException {

    public static final String MESSAGE = "SMS code mismatch or expired.";

    public CodeMismatch() {
        super(MESSAGE);
    }

    public CodeMismatch(String s) {
        super(s);
    }
}

