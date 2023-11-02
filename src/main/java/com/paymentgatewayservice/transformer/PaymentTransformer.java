package com.paymentgatewayservice.transformer;

import com.paymentgatewayservice.dto.PaymentRequest;
import com.paymentgatewayservice.model.Payment;
import org.springframework.stereotype.Component;


@Component
public class PaymentTransformer {
    public Payment transformToEntity(PaymentRequest paymentRequest, String transactionId) {
        return Payment.builder()
                .payerId(paymentRequest.getPayerId())
                .payeeId(paymentRequest.getPayeeId())
                .amount(paymentRequest.getAmount())
                .currency(paymentRequest.getCurrency())
                .transactionId(transactionId)
                .status("processing")
                .build();
    }
}
