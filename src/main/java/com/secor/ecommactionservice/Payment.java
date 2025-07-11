package com.secor.ecommactionservice;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
public class Payment
{
    @Id
    private String paymentId;

    private String orderId;
    private String customerId;
    private String paymentMethod;
    private BigDecimal amount;
    private String status; // PENDING, COMPLETED, FAILED, REFUND_INITIATED,REFUNDED
    private LocalDateTime paymentDate;
    private LocalDateTime updatedAt;
}