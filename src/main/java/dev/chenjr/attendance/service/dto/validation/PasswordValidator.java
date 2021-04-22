package dev.chenjr.attendance.service.dto.validation;

public class PasswordValidator extends PatternValidator<Password> {

    public static final String RE_PASSWORD = "^[a-zA-Z0-9~!@#$%^&*()+{}|:\"\\<\\>?,./;']{6,20}$";

    @Override
    public void initialize(Password constraintAnnotation) {
        super.initialize(constraintAnnotation);
        this.addPatternExp(RE_PASSWORD);
    }
}
