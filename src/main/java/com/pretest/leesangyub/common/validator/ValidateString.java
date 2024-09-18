package com.pretest.leesangyub.common.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Constraint(validatedBy = {StringValidator.class})
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidateString {

    boolean nullPass() default true;

    String[] acceptedValues();

    String message() default "Invalid string value. This is not permitted.";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
