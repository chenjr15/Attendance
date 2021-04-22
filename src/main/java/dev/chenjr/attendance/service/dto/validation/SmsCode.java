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
@Constraint(validatedBy = SmsCodeValidator.class)
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface SmsCode {
    String message() default "仅支持4-6位数字";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};


}