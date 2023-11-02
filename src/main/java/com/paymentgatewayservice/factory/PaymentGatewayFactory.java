package com.paymentgatewayservice.factory;

import com.paymentgatewayservice.gateway.PayPalGateway;
import com.paymentgatewayservice.gateway.PaymentGatewayProcess;
import com.paymentgatewayservice.gateway.StripeGateway;
import org.springframework.stereotype.Component;

@Component
public class PaymentGatewayFactory {

    public static PaymentGatewayProcess createPaymentGateway(String gatewayName) {
        switch (gatewayName) {
            case "PayPal":
                return new PayPalGateway();
            case "Stripe":
                return new StripeGateway();
            default:
                throw new IllegalArgumentException("Invalid payment gateway: " + gatewayName);
        }
    }
}
