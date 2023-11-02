package com.paymentgatewayservice.service;

import com.paymentgatewayservice.dto.PaymentRequest;
import com.paymentgatewayservice.dto.PaymentResponse;


public interface PaymentService {
    PaymentResponse initiatePayment(PaymentRequest paymentRequest);
}
