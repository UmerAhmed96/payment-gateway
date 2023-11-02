package com.paymentgatewayservice.service;

import com.paymentgatewayservice.dto.PaymentRequest;
import com.paymentgatewayservice.dto.PaymentResponse;
import com.paymentgatewayservice.exception.DuplicatePaymentException;
import com.paymentgatewayservice.gateway.PaymentGatewayProcess;
import com.paymentgatewayservice.model.Payment;
import com.paymentgatewayservice.model.PaymentGateway;
import com.paymentgatewayservice.repository.PaymentGatewayRepository;
import com.paymentgatewayservice.repository.PaymentRepository;
import com.paymentgatewayservice.service.PaymentServiceImpl;
import com.paymentgatewayservice.transformer.PaymentTransformer;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PaymentServiceTest {

    @Test
    void testInitiatePayment_SuccessfulPayment() {
        // Mocking dependencies
        PaymentGatewayRepository paymentGatewayRepository = mock(PaymentGatewayRepository.class);
        PaymentRepository paymentRepository = mock(PaymentRepository.class);
        PaymentTransformer paymentTransformer = mock(PaymentTransformer.class);

        // Creating an instance of PaymentServiceImpl
        PaymentServiceImpl paymentService = new PaymentServiceImpl(paymentGatewayRepository, paymentRepository, paymentTransformer);

        // Creating a sample PaymentRequest
        PaymentRequest paymentRequest = PaymentRequest.builder()
                .payerId("user123")
                .payeeId("store123")
                .amount(200.00)
                .currency("USD")
                .preferredGateway("PayPal")
                .build();

        // Mocking behavior for dependencies
        when(paymentRepository.findByTransactionId(anyString())).thenReturn(Optional.empty());
        when(paymentTransformer.transformToEntity(any(PaymentRequest.class), anyString())).thenReturn(new Payment());
        when(paymentGatewayRepository.findAll()).thenReturn(List.of(new PaymentGateway()));

        // Mocking PaymentGatewayProcess
        PaymentGatewayProcess paymentGatewayProcess = mock(PaymentGatewayProcess.class);
        when(paymentGatewayProcess.processPayment(any())).thenReturn(true);
        when(paymentGatewayRepository.findByName(anyString())).thenReturn(Optional.of(new PaymentGateway()));

        // Invoking the method to test
        PaymentResponse paymentResponse = paymentService.initiatePayment(paymentRequest);

        // Verifying the result
        assertEquals("success", paymentResponse.getStatus());
        verify(paymentRepository, times(1)).save(any());
    }

    @Test
    void testInitiatePayment_DuplicateTransaction() {
        // Mocking dependencies
        PaymentGatewayRepository paymentGatewayRepository = mock(PaymentGatewayRepository.class);
        PaymentRepository paymentRepository = mock(PaymentRepository.class);
        PaymentTransformer paymentTransformer = mock(PaymentTransformer.class);

        // Creating an instance of PaymentServiceImpl
        PaymentServiceImpl paymentService = new PaymentServiceImpl(paymentGatewayRepository, paymentRepository, paymentTransformer);

        // Creating a sample PaymentRequest
        PaymentRequest paymentRequest = PaymentRequest.builder()
                .payerId("user123")
                .payeeId("store456")
                .amount(200.00)
                .currency("USD")
                .preferredGateway("PayPal")
                .build();

        // Mocking behavior for dependencies
        when(paymentRepository.findByTransactionId(anyString())).thenReturn(Optional.of(new Payment()));

        // Asserting that DuplicatePaymentException is thrown
        assertThrows(DuplicatePaymentException.class, () -> paymentService.initiatePayment(paymentRequest));

        // Verifying interactions
        verify(paymentRepository, never()).save(any());
    }

    @Test
    void testInitiatePayment_FailedPayment() {
        // Mocking dependencies
        PaymentGatewayRepository paymentGatewayRepository = mock(PaymentGatewayRepository.class);
        PaymentRepository paymentRepository = mock(PaymentRepository.class);
        PaymentTransformer paymentTransformer = mock(PaymentTransformer.class);

        // Creating an instance of PaymentServiceImpl
        PaymentServiceImpl paymentService = new PaymentServiceImpl(paymentGatewayRepository, paymentRepository, paymentTransformer);

        // Creating a sample PaymentRequest
        PaymentRequest paymentRequest = PaymentRequest.builder()
                .payerId("user123")
                .payeeId("store456")
                .amount(200.00)
                .currency("USD")
                .preferredGateway("PayPal")
                .build();

        // Mocking behavior for dependencies
        when(paymentRepository.findByTransactionId(anyString())).thenReturn(Optional.empty());
        when(paymentTransformer.transformToEntity(any(PaymentRequest.class), anyString())).thenReturn(new Payment());
        when(paymentGatewayRepository.findAll()).thenReturn(List.of(new PaymentGateway()));

        // Mocking PaymentGatewayProcess to simulate a failed payment
        PaymentGatewayProcess paymentGatewayProcess = mock(PaymentGatewayProcess.class);
        when(paymentGatewayProcess.processPayment(any())).thenReturn(false);
        when(paymentGatewayRepository.findByName(anyString())).thenReturn(Optional.of(new PaymentGateway()));

        // Invoking the method to test
        PaymentResponse paymentResponse = paymentService.initiatePayment(paymentRequest);

        // Verifying the result
        assertEquals("failed", paymentResponse.getStatus());
        verify(paymentRepository, times(1)).save(any());
    }

    @Test
    void testInitiatePayment_InvalidTransactionId() {
        // Mocking dependencies
        PaymentGatewayRepository paymentGatewayRepository = mock(PaymentGatewayRepository.class);
        PaymentRepository paymentRepository = mock(PaymentRepository.class);
        PaymentTransformer paymentTransformer = mock(PaymentTransformer.class);

        // Creating an instance of PaymentServiceImpl
        PaymentServiceImpl paymentService = new PaymentServiceImpl(paymentGatewayRepository, paymentRepository, paymentTransformer);

        // Creating a sample PaymentRequest
        PaymentRequest paymentRequest = PaymentRequest.builder()
                .payerId("user123")
                .payeeId("store456")
                .amount(200.00)
                .currency("USD")
                .preferredGateway("PayPal")
                .build();

        // Mocking behavior for dependencies
        when(paymentRepository.findByTransactionId(anyString())).thenReturn(Optional.of(new Payment()));

        // Asserting that DuplicatePaymentException is thrown
        assertThrows(DuplicatePaymentException.class, () -> paymentService.initiatePayment(paymentRequest));

        // Verifying interactions
        verify(paymentRepository, never()).save(any());
    }
}
