package dev.chenjr.attendance.service.dto.validation;

public class PhoneNumberValidator extends PatternValidator<PhoneNumber> {

    public static final String RE_PHONE_NUMBER = "^[1](([3][0-9])|([4][5-9])|([5][0-3,5-9])|([6][5,6])|([7][0-8])|([8][0-9])|([9][1,8,9]))[0-9]{8}$";

    @Override
    public void initialize(PhoneNumber constraintAnnotation) {
        super.initialize(constraintAnnotation);
        this.addPatternExp(RE_PHONE_NUMBER);
    }
}
