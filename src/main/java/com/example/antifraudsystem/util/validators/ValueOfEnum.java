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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Constraint(validatedBy = ValueOfEnum.Validator.class)
public @interface ValueOfEnum {
    Class<? extends Enum<?>> enumClass();

    java.lang.String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class Validator implements ConstraintValidator<ValueOfEnum, String> {
        private List<String> enumValues;

        @Override
        public void initialize(ValueOfEnum constraintAnnotation) {
            enumValues = Arrays.stream(constraintAnnotation.enumClass().getEnumConstants())
                    .map(Enum::name)
                    .collect(Collectors.toList());
        }

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            return value != null && enumValues.contains(value);
        }
    }
}
