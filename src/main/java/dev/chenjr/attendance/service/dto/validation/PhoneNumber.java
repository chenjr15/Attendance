package dev.chenjr.attendance.service.dto.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = PhoneNumberValidator.class)
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface PhoneNumber {
    String message() default "仅支持中国大陆有效手机号";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};


}