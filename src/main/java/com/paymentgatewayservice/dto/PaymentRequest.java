package com.paymentgatewayservice.dto;

import com.paymentgatewayservice.validator.ValidCurrency;
import com.paymentgatewayservice.validator.ValidGateway;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {

    @NotBlank(message = "Payer ID is required")
    private String payerId;

    @NotBlank(message = "Payee ID is required")
    private String payeeId;

    @Positive(message = "Amount must be positive")
    private double amount;

    @ValidCurrency  // Applied the custom annotation
    private String currency;


    @ValidGateway // Applied the custom annotation
    private String preferredGateway;
}

