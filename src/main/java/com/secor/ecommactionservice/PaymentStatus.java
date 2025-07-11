package com.secor.ecommactionservice;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class PaymentStatus {
    private String status;
    private String orderId;
    private String customerId;
}
