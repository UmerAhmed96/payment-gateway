package com.paymentgatewayservice.service;

import com.paymentgatewayservice.dto.PaymentRequest;
import com.paymentgatewayservice.dto.PaymentResponse;
import com.paymentgatewayservice.exception.DuplicatePaymentException;
import com.paymentgatewayservice.factory.PaymentGatewayFactory;
import com.paymentgatewayservice.gateway.PaymentGatewayProcess;
import com.paymentgatewayservice.model.Payment;
import com.paymentgatewayservice.model.PaymentGateway;
import com.paymentgatewayservice.repository.PaymentGatewayRepository;
import com.paymentgatewayservice.repository.PaymentRepository;
import com.paymentgatewayservice.transformer.PaymentTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
public class PaymentServiceImpl implements PaymentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentServiceImpl.class);

    private final PaymentRepository paymentRepository;

    private final PaymentTransformer paymentTransformer;

    private final PaymentGatewayRepository paymentGatewayRepository;

    public PaymentServiceImpl(PaymentGatewayRepository paymentGatewayRepository, PaymentRepository paymentRepository, PaymentTransformer paymentTransformer) {
        this.paymentGatewayRepository = paymentGatewayRepository;
        this.paymentRepository = paymentRepository;
        this.paymentTransformer = paymentTransformer;
    }

    @Override
    @Transactional
    public PaymentResponse initiatePayment(PaymentRequest paymentRequest) {
        LOGGER.info("Payment initiated");

        String transactionId = generateTransactionId();

        // Check if transaction with this ID already exists (double spending prevention)
        checkForDuplicateTransaction(transactionId);

        // Transform DTO to entity using builder pattern
        Payment payment = paymentTransformer.transformToEntity(paymentRequest, transactionId);

        // Processing payment through preferred payment gateway asynchronously with timeout
        CompletableFuture<Boolean> paymentFuture = CompletableFuture.supplyAsync(() ->
                processPaymentWithRetry(paymentRequest));

        boolean success;
        try {
            success = paymentFuture.get(5, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {

            LOGGER.error("Timeout exception occur");
            LOGGER.error(e.getMessage());
            success = false;
        }

        // Updating payment status based on processing result
        payment.setStatus(success ? "success" : "failed");
        paymentRepository.save(payment);

        return PaymentResponse.builder()
                .transactionId(transactionId)
                .status(payment.getStatus())
                .build();

    }

    private String generateTransactionId() {
        return UUID.randomUUID().toString();
    }

    private void checkForDuplicateTransaction(String transactionId) {
        if (paymentRepository.findByTransactionId(transactionId).isPresent()) {
            throw new DuplicatePaymentException("Duplicate transaction ID detected.");
        }
    }


    private boolean processPaymentWithRetry(PaymentRequest paymentRequest) {
        // Retrieve all available payment gateways
        List<PaymentGateway> allGateways = paymentGatewayRepository.findAll();
        List<String> availableGateways = allGateways.stream().map(PaymentGateway::getName).toList();

        List<String> attemptedGateways = new ArrayList<>();

        // Attempt preferred gateway first
        PaymentGatewayProcess preferredGateway = PaymentGatewayFactory.createPaymentGateway(paymentRequest.getPreferredGateway());
        attemptedGateways.add(paymentRequest.getPreferredGateway());

        try {
            if (preferredGateway.processPayment(paymentRequest)) {
                return true;
            }else {

                // If preferred gateway fails, try alternative gateways
                for (String gateway : availableGateways) {
                    if (!attemptedGateways.contains(gateway)) {
                        try {
                            PaymentGatewayProcess gatewayProcess = PaymentGatewayFactory.createPaymentGateway(gateway);
                            attemptedGateways.add(gateway);
                            if (gatewayProcess.processPayment(paymentRequest)) {
                                return true;
                            }
                        } catch (Exception e) {
                            LOGGER.error(e.getMessage());
                        }
                    }
                }

            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }

        return false; //all attempts are failed
    }

}

