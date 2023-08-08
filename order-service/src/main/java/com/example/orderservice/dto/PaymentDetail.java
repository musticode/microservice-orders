package com.example.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.Instant;

@Builder
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class PaymentDetail {
    private long paymentId;
    private String paymentMode;
    private String paymentStatus;
    private Instant paymentDate;
}
