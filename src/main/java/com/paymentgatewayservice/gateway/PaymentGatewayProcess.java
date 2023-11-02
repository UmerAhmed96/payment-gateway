package com.paymentgatewayservice.gateway;

import com.paymentgatewayservice.dto.PaymentRequest;
import com.paymentgatewayservice.dto.PaymentResponse;
import org.springframework.stereotype.Component;

@Component
public interface PaymentGatewayProcess {
    boolean processPayment(PaymentRequest paymentRequest);
}
