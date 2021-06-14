package dev.chenjr.attendance.service.dto.validation;

public class LoginNameValidator extends PatternValidator<LoginName> {

    public static final String RE_LOGIN_NAME = "^[a-zA-Z]([a-zA-Z0-9_]{3,19})$";

    @Override
    public void initialize(LoginName constraintAnnotation) {
        super.initialize(constraintAnnotation);
        this.addPatternExp(RE_LOGIN_NAME);
    }
}
