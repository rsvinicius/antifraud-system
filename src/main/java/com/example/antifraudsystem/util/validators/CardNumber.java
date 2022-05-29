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

import static com.example.antifraudsystem.util.constants.Numbers.ONE;
import static com.example.antifraudsystem.util.constants.Numbers.TEN;
import static com.example.antifraudsystem.util.constants.Numbers.TWO;
import static com.example.antifraudsystem.util.constants.Numbers.ZERO;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = CardNumber.Validator.class)
public @interface CardNumber {

    char CHAR_ZERO = '0';

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
            int sum = ZERO;
            boolean isSecond = false;

            for (int i = nDigits - ONE; i >= ZERO; i--) {
                int d = cardNumber.charAt(i) - CHAR_ZERO;

                if (isSecond) d = d * TWO;

                sum += d / TEN;
                sum += d % TEN;

                isSecond = !isSecond;
            }
            return (sum % TEN == ZERO);
        }
    }
}
