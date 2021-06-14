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
@Constraint(validatedBy = LoginNameValidator.class)
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface LoginName {
    String message() default "登录名只能是4-20个英文字符、数字和下划线，不能以数字和下划线开头";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};


}
