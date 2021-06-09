package dev.chenjr.attendance.service.dto.validation;

public class KeyWordValidator extends PatternValidator<KeyWord> {

    public static final String RE_KEYWORD = "^[a-zA-Z]([a-zA-Z0-9_]{2,255})$";

    @Override
    public void initialize(KeyWord constraintAnnotation) {
        super.initialize(constraintAnnotation);
        this.addPatternExp(RE_KEYWORD);
    }
}
