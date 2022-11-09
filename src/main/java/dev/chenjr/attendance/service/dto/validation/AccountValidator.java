package dev.chenjr.attendance.service.dto.validation;

import static dev.chenjr.attendance.service.dto.validation.LoginNameValidator.RE_LOGIN_NAME;
import static dev.chenjr.attendance.service.dto.validation.PhoneNumberValidator.RE_PHONE_NUMBER;

public class AccountValidator extends PatternValidator<Account> {


    public static final String RE_EMAIL = "^[a-zA-Z0-9]{2,19}@([a-zA-Z0-9]+\\.)+[a-zA-Z0-9]{2,}$";
    public static final String RE_ACCOUNT = "(" + RE_LOGIN_NAME + ")|(" + RE_PHONE_NUMBER + ")|(" + RE_EMAIL + ")";

    @Override
    public void initialize(Account constraintAnnotation) {
        super.initialize(constraintAnnotation);

        this.addPatternExp(RE_LOGIN_NAME, RE_PHONE_NUMBER, RE_EMAIL);
    }
}
