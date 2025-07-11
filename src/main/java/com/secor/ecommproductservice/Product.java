package com.secor.ecommproductservice;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "productcatalog")
@Getter
@Setter
public class Product
{
    @Id
    private String productId;

    private String productName;
    private String description;
    private BigDecimal price;
    private String category;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}