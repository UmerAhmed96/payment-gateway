package com.paymentgatewayservice.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = GatewayValidator.class)
@Target( { ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidGateway {
    String message() default "Invalid gateway. Only PayPal and Stripe are accepted.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

