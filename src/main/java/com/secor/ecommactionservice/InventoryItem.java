package com.secor.ecommactionservice;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


import org.springframework.data.annotation.Id;

@Getter
@Setter
public class InventoryItem
{
    @Id
    private String inventoryId;

    private String productId;
    private Integer quantity;

    private LocalDateTime lastUpdated;
}
