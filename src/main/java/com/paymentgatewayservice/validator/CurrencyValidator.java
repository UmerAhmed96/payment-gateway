package com.paymentgatewayservice.validator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class CurrencyValidator implements ConstraintValidator<ValidCurrency, String> {

    private final List<String> validCurrencies = Arrays.asList("USD", "PKR");

    @Override
    public void initialize(ValidCurrency constraintAnnotation) {}

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && validCurrencies.contains(value.toUpperCase());
    }
}

