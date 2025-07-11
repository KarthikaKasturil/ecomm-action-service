package com.secor.ecommactionservice;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Getter
@Setter
public class Order
{
    @Id
    private String orderId;

    private String customerId;
    private String productId;
    private Integer quantity;

    private LocalDateTime orderDate;
    private String status; // PENDING_PAYMENT, CONFIRMED, COMPLETED, CANCELLED
    private BigDecimal totalAmount;
    private LocalDateTime updatedAt;
}