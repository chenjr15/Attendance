package dev.chenjr.attendance.service.dto.validation;

import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
public class PatternValidator<T extends Annotation> implements ConstraintValidator<T, String> {


    private final List<Pattern> patterns = new ArrayList<>();
    public void addPattern(Pattern... pattern) {
        this.patterns.addAll(Arrays.asList(pattern));
    }


    public void addPatternExp(String... exp) {
        this.patterns.addAll(Arrays.stream(exp).map(Pattern::compile).collect(Collectors.toList()));
    }

    /**
     * Initializes the validator in preparation for
     * {@link #isValid(String, ConstraintValidatorContext)}  calls.
     * The constraint annotation for a given constraint declaration
     * is passed.
     * <p>
     * This method is guaranteed to be called before any use of this instance for
     * validation.
     * <p>
     * The default implementation is a no-op.
     *
     * @param constraintAnnotation annotation instance for a given constraint declaration
     */
    @Override
    public void initialize(T constraintAnnotation) {
    }

    /**
     * 注意这个验证不管空指针(null)，
     * 空指针需要单独用{@link javax.validation.constraints.NotNull}或{@link javax.validation.constraints.NotBlank}判断
     *
     * @param value   object to validate
     * @param context context in which the constraint is evaluated
     * @return {@code false} if {@code value} does not pass the constraint
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return patterns.stream().anyMatch(pattern -> pattern.matcher(value).matches());
    }
}
