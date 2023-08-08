package com.example.orderservice.service.impl;

import com.example.orderservice.client.PaymentService;
import com.example.orderservice.client.ProductService;
import com.example.orderservice.dto.*;
import com.example.orderservice.exception.CustomException;
import com.example.orderservice.model.Order;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.Instant;

import static com.example.orderservice.constant.Constant.PAYMENT_SERVICE_ENDPOINT;
import static com.example.orderservice.constant.Constant.PRODUCT_SERVICE_ENDPOINT;

@Service
@RequiredArgsConstructor
@Log4j2
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final PaymentService paymentService;
    private final RestTemplate restTemplate;

    @Override
    public long placeOrder(OrderRequest orderRequest) {
        log.info("Placing order request: {}", orderRequest);

        // TODO reduce quantity from PRODUCT-SERVICE
        productService.reduceQuantity(orderRequest.getProductId(), orderRequest.getQuantity());


        log.info("Creating order with status CREATED");

        // TODO order entity -> save the data with status order created
        Order order = Order.builder()
                .amount(orderRequest.getTotalAmount())
                .orderStatus("CREATED")
                .productId(orderRequest.getProductId())
                .orderDate(Instant.now())
                .quantity(orderRequest.getQuantity())
                .build();

        order = orderRepository.save(order);




        // TODO payment service-> payments -> succss-> complete, else cancel
        log.info("calling payment service to complete the payment...");
        PaymentRequest paymentRequest = PaymentRequest.builder()
                .orderId(order.getId())
                .amount(orderRequest.getTotalAmount())
                .build();

        String orderStatus  = null;

        try {
            paymentService.doPayment(paymentRequest);

            log.info("Payment done successfully.... Changing the order status");

            orderStatus = "PLACED";
        }catch (Exception e ){
            log.error("Error occured in payment. Changed order status");
            orderStatus = "ERROR";
        }

        order.setOrderStatus(orderStatus);
        orderRepository.save(order);


        return order.getId();
    }

    @Override
    public OrderResponse getOrderDetail(long orderId) {


        Order order = orderRepository
                .findById(orderId)
                .orElseThrow(()-> new CustomException("No order", "NOT_FOUND", 404)
                );


        ProductDetail productDetail = restTemplate
                .getForObject(PRODUCT_SERVICE_ENDPOINT + order.getProductId(), ProductDetail.class);


        log.info("Getting payment information...");
        PaymentResponse paymentResponse = restTemplate
                .getForObject(PAYMENT_SERVICE_ENDPOINT + order.getId(), PaymentResponse.class);

        PaymentDetail paymentDetail = PaymentDetail.builder()
                .paymentId(paymentResponse.getPaymentId())
                .paymentMode(paymentResponse.getPaymentMode())
                .paymentStatus(paymentResponse.getStatus())
                .paymentDate(paymentResponse.getPaymentDate())
                .build();


        return OrderResponse.builder()
                .orderId(order.getId())
                .orderDate(order.getOrderDate())
                .orderStatus(order.getOrderStatus())
                .amount(order.getAmount())
                .productDetail(productDetail)
                .paymentDetail(paymentDetail)
                .build();
    }


}
