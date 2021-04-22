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

//    private static final String regexp = ;


    private final List<Pattern> patterns = new ArrayList<>();

    //    public PatternValidator(String regexp) {
    //        this.regexp = regexp;
    //    }
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
     * Implements the validation logic.
     * The state of {@code value} must not be altered.
     * <p>
     * This method can be accessed concurrently, thread-safety must be ensured
     * by the implementation.
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
        //        log.info("payload:{}", Arrays.toString(payload));
//        context.unwrap(HibernateConstraintValidatorContext.class)
//                .addMessageParameter("aaa", "ccc")
//                .buildConstraintViolationWithTemplate("aaa:{aaa}, doldlor aaa:${aaa}");
        return patterns.stream().anyMatch(pattern -> pattern.matcher(value).matches());
    }
}
