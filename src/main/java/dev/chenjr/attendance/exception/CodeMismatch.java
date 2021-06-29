package dev.chenjr.attendance.exception;

public class CodeMismatch extends SmsException {
    
    public static final String MESSAGE = "短信验证码不匹配或者已过期！";
    
    public CodeMismatch() {
        super(MESSAGE);
    }
    
    public CodeMismatch(String s) {
        super(s);
    }
}

