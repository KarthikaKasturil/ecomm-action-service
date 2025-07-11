package com.secor.ecommactionservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
public class ActionService
{
    private static final Logger logger = LoggerFactory.getLogger(ActionService.class);


    @Autowired
    @Qualifier("productWebClient")
    WebClient productWebClient;

    @Autowired
    @Qualifier("inventoryWebClient")
    WebClient inventoryWebClient;

    @Autowired
    @Qualifier("orderWebClient")
    WebClient orderWebClient;

    @Autowired
    @Qualifier("customerWebClient")
    WebClient customerWebClient;

    @Autowired
    @Qualifier("paymentWebClient")
    WebClient paymentWebClient;

    public Order getOrderDetails(String orderId) throws WebClientResponseException
    {
        logger.info("Call order service for details");

        return orderWebClient.get()
                .uri("/" + orderId)
                .retrieve()
                .bodyToMono(Order.class)
                .block(); // Assuming the product details are valid for demonstration purposes
    }

    public InventoryItem checkInventory(String productId) throws WebClientResponseException
    {
        logger.info("Call check inventory service");

        return inventoryWebClient.get()
                .uri("/product/" + productId)
                .retrieve()
                .bodyToMono(InventoryItem.class)
                .block(); // Assuming the inventory check is valid for demonstration purposes
    }

    public InventoryItem updateInventory(InventoryItem itemDetails) throws WebClientResponseException
    {
        logger.info("Call update inventory service");

        return inventoryWebClient.put()
                .uri("/update/product")
                .bodyValue(itemDetails)
                .retrieve()
                .bodyToMono(InventoryItem.class)
                .block();// Assuming the inventory check is valid for demonstration purposes
    }

    public Order saveOrder(Order order) throws WebClientResponseException
    {
        logger.info("Call  to save order");

        return orderWebClient.post()
                .uri("/save")
                .bodyValue(order)
                .retrieve()
                .bodyToMono(Order.class)
                .block();// Assuming the inventory check is valid for demonstration purposes
    }

    public Payment savePayment(Payment payment) throws WebClientResponseException
    {
        logger.info("Call  to save payment");

        return paymentWebClient.post()
                .uri("/save")
                .bodyValue(payment)
                .retrieve()
                .bodyToMono(Payment.class)
                .block();// Assuming the inventory check is valid for demonstration purposes
    }

    public Payment getPaymentByOrder(String orderId) throws WebClientResponseException
    {
        logger.info("Call  to get payment by order");

        return paymentWebClient.get()
                .uri("/order/" + orderId)
                .retrieve()
                .bodyToMono(Payment.class)
                .block();
    }

    public ResponseEntity<String> updateOrderStatus(OrderStatus status) throws WebClientResponseException
    {
        logger.info("Call update status of order");

        return orderWebClient.put()
                .uri("/update/status")
                .bodyValue(status)
                .retrieve()
                .toEntity(String.class)
                .block();// Assuming the inventory check is valid for demonstration purposes
    }

    public ResponseEntity<String> updateCustomerOrder(OrderInfo info) throws WebClientResponseException
    {
        logger.info("Call update order for customer");

        return customerWebClient.put()
                .uri("/update/order")
                .bodyValue(info)
                .retrieve()
                .toEntity(String.class)
                .block();// Assuming the inventory check is valid for demonstration purposes
    }

    public ResponseEntity<String> createPayment(Payment payment) throws WebClientResponseException
    {
        logger.info("Call payment service");

        return paymentWebClient.post()
                .uri("/create")
                .bodyValue(payment)
                .retrieve()
                .toEntity(String.class)
                .block();// Assuming the inventory check is valid for demonstration purposes
    }

    public Product getProductDetails(String productId) throws WebClientResponseException
    {
        logger.info("Call product service");

        return productWebClient.get()
                .uri("/" + productId)
                .retrieve()
                .bodyToMono(Product.class)
                .block(); // Assuming the product details are valid for demonstration purposes
    }
}
