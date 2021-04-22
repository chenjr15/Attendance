package dev.chenjr.attendance.service.dto.validation;

public class PhoneNumberValidator extends PatternValidator<PhoneNumber> {

    public static final String RE_PHONE_NUMBER = "^(\\+86)?1[3-9]\\d{9}$";

    @Override
    public void initialize(PhoneNumber constraintAnnotation) {
        super.initialize(constraintAnnotation);
        this.addPatternExp(RE_PHONE_NUMBER);
    }
}
