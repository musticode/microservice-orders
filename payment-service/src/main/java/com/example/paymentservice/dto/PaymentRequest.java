package com.example.paymentservice.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PaymentRequest {
    private long orderId;
    private long amount;
    private String referenceNumber;
}
