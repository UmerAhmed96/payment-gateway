// PaymentControllerTest.java
package com.paymentgatewayservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymentgatewayservice.dto.PaymentRequest;
import com.paymentgatewayservice.dto.PaymentResponse;
import com.paymentgatewayservice.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PaymentService paymentService;



    @Test
    void testInitiatePayment() throws Exception {
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setPayerId("payer123");
        paymentRequest.setPayeeId("payee456");
        paymentRequest.setAmount(100.00);
        paymentRequest.setCurrency("USD");
        paymentRequest.setPreferredGateway("PayPal");

        String requestJson = objectMapper.writeValueAsString(paymentRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/payments/initiate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.transactionId").exists())
                .andExpect(jsonPath("$.status").value("success"));
    }
}
