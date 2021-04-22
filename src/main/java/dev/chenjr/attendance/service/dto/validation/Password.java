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
@Constraint(validatedBy = PasswordValidator.class)
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface Password {
    String message() default "密码需要在6-20位，字母下划线和~!@#$%^&*()+{}|:\"<>?,./;";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};


}
