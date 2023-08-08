package com.example.paymentservice.service.impl;

import com.example.paymentservice.dto.PaymentRequest;
import com.example.paymentservice.dto.PaymentResponse;
import com.example.paymentservice.model.TransactionDetails;
import com.example.paymentservice.repository.PaymentRepository;
import com.example.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Log4j2
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;


    @Override
    public Long doPayment(PaymentRequest paymentRequest) {
        log.info("Recording payment details {}", paymentRequest);

        TransactionDetails transactionDetails = TransactionDetails.builder()
                .paymentDate(Instant.now())
                .paymentStatus("SUCCESS")
                .referenceNumber(paymentRequest.getReferenceNumber())
                .amount(paymentRequest.getAmount())
                .orderId(paymentRequest.getOrderId())
                .build();

        paymentRepository.save(transactionDetails);

        log.info("saved: {}", transactionDetails.getId());

        return null;
    }

    @Override
    public PaymentResponse getPaymentDetilsByOrderId(Long orderId) {
        log.info("Getting payment details for the order id: {}", orderId);

        TransactionDetails transactionDetails = paymentRepository
                .findByOrderId(Long.valueOf(orderId));


        return PaymentResponse.builder()
                .paymentId(transactionDetails.getId())
                .status(transactionDetails.getPaymentStatus())
                .paymentMode(transactionDetails.getPaymentMode())
                .amount(transactionDetails.getAmount())
                .paymentDate(transactionDetails.getPaymentDate())
                .orderId(transactionDetails.getOrderId())
                .build();
    }
}
