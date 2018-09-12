package com.quakearts.demo.rest.beans;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class GreetingValidator implements ConstraintValidator<CheckGreeting, String> {

	@Override
	public void initialize(CheckGreeting constraintAnnotation) {
		//Does nothing
	}

	@Override
	public boolean isValid(String greeting, ConstraintValidatorContext context) {
		return greeting.matches(".*\\{0\\}.*$");
	}

}
