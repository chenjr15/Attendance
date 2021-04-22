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
@Constraint(validatedBy = AccountValidator.class)
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface Account {
    String message() default "帐户只能是用户名、密码或者手机号";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
