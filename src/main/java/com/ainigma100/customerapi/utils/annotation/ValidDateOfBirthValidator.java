package com.ainigma100.customerapi.utils.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Period;

public class ValidDateOfBirthValidator implements ConstraintValidator<ValidDateOfBirth, LocalDate> {

    private int minAge;

    @Override
    public void initialize(ValidDateOfBirth constraintAnnotation) {
        this.minAge = constraintAnnotation.minAge();
    }

    @Override
    public boolean isValid(LocalDate dateOfBirth, ConstraintValidatorContext context) {


        if (dateOfBirth == null) {
            return false;
        }

        LocalDate today = LocalDate.now();
        int age = Period.between(dateOfBirth, today).getYears();

        return !dateOfBirth.isAfter(today) && age >= minAge;
    }
}
