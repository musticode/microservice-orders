package com.example.productservice.service.impl;

import com.example.productservice.dto.ProductRequest;
import com.example.productservice.dto.ProductResponse;
import com.example.productservice.model.Product;
import com.example.productservice.repository.ProductRepository;
import com.example.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
@Log4j2
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;


    @Override
    public long addProduct(ProductRequest productRequest) {
        log.info("Adding product..." +productRequest.getName());

        Product product = Product.builder()
                .productName(productRequest.getName())
                .price(productRequest.getPrice())
                .quantity(productRequest.getQuantity())
                .build();
        productRepository.save(product);

        log.info("added..");

        return product.getProductId();
    }

    @Override
    public ProductResponse getProductById(Long productId) {

        Product product = productRepository
                .findById(productId)
                .orElseThrow(()-> new RuntimeException("No product with "+ productId));

        ProductResponse productResponse = ProductResponse.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .build();


        return productResponse;
    }

    @Override
    public void reduceQuantity(long id, long quantity) {
        log.info("Reduce quantity {} for id {}", quantity, id);

        Product product = productRepository
                .findById(id)
                .orElseThrow(()-> new RuntimeException("No product with "+ id));


        if (product.getQuantity() < quantity){
            throw new RuntimeException("insufficient quantity");
        }
        product.setQuantity(product.getQuantity() - quantity);

        productRepository.save(product);
        log.info("product quantity updated successfully");


    }
}
