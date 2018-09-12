package com.quakearts.demo.rest.beans;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class GreetingValidator implements ConstraintValidator<CheckGreeting, String> {

	@Override
	public void initialize(CheckGreeting constraintAnnotation) {
	}

	@Override
	public boolean isValid(String greeting, ConstraintValidatorContext context) {
		boolean value = greeting.matches(".*\\{0\\}.*$");
		return value;
	}

}
