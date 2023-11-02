package com.paymentgatewayservice.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CurrencyValidator.class)
@Target( { ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCurrency {
    String message() default "Invalid currency. Only USD and PKR are accepted.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
