package com.paymentgatewayservice.exception.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Data
public class ApiError {
    private HttpStatus status;
    private String message;
}
