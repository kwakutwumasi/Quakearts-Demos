package com.quakearts.demo.rest.beans;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Retention(RUNTIME)
@Target({ TYPE, METHOD, PARAMETER, ANNOTATION_TYPE })
@Constraint(validatedBy=GreetingValidator.class)
public @interface CheckGreeting {
    String message() default "Invalid greeting";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default{};
}
