package com.example.antifraudsystem.util.validators;

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
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = Ip.Validator.class)
public @interface Ip {
    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class Validator implements ConstraintValidator<Ip, String> {
        String IP_ADDRESS_REGEX = "((\\d{1,2}|1\\d{1,2}|2([0-4]\\d|5[0-5]))(\\.(?!$)|$)){4}";

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            return value != null && value.matches(IP_ADDRESS_REGEX);
        }
    }
}
