package dev.chenjr.attendance.service.dto.validation;

public class SmsCodeValidator extends PatternValidator<SmsCode> {

    public static final String RE_SMS_CODE = "$\\d{4,6}^";

    @Override
    public void initialize(SmsCode constraintAnnotation) {
        super.initialize(constraintAnnotation);
        this.addPatternExp(RE_SMS_CODE);
    }
}
