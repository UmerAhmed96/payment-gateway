package com.paymentgatewayservice.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class GatewayValidator implements ConstraintValidator<ValidGateway, String> {

    private final List<String> validGateways = Arrays.asList("PayPal", "Stripe");

    @Override
    public void initialize(ValidGateway constraintAnnotation) {}

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && validGateways.contains(value);
    }
}

