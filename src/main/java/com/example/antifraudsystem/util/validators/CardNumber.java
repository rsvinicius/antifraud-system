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
@Constraint(validatedBy = CardNumber.Validator.class)
public @interface CardNumber {
    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class Validator implements ConstraintValidator<CardNumber, String> {
        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            return (value != null) ? checkLuhn(value) : false;
        }

        private Boolean checkLuhn(String cardNumber) {
            int nDigits = cardNumber.length();
            int sum = 0;
            boolean isSecond = false;

            for (int i = nDigits - 1; i >= 0; i--) {
                int d = cardNumber.charAt(i) - '0';

                if (isSecond) d = d * 2;

                sum += d / 10;
                sum += d % 10;

                isSecond = !isSecond;
            }
            return (sum % 10 == 0);
        }
    }
}
