package dev.chenjr.attendance.service.dto.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = KeyWordValidator.class)
@Target({METHOD, FIELD, PARAMETER})
@Retention(RUNTIME)
public @interface KeyWord {
    String message() default "关键字长度最少为3位，只能是数字字母和下划线，不能以下划线和数字开头";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
