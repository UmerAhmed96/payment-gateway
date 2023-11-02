package com.paymentgatewayservice.gateway;

import com.paymentgatewayservice.dto.PaymentRequest;
import com.paymentgatewayservice.dto.PaymentResponse;
import org.springframework.stereotype.Service;

@Service
public class StripeGateway implements PaymentGatewayProcess {

    @Override
    public boolean processPayment(PaymentRequest paymentRequest) {
        //  If payment is successful return true otherwise return false
        return true;
    }
}
