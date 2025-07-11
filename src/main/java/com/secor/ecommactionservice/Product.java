package com.secor.ecommactionservice;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Getter
@Setter
public class Product
{
    private String productId;

    private String productName;
    private String description;
    private BigDecimal price;
    private String category;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}