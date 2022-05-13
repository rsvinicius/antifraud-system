package com.example.antifraudsystem.annotations;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Constraint(validatedBy = Amount.Validator.class)
public @interface Amount {
    String message() default "Amount must be greater than 0!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    class Validator implements ConstraintValidator<Amount, Long> {

        @Override
        public boolean isValid(Long value, ConstraintValidatorContext context) {
            return value != null && value > 0;
        }
    }
}