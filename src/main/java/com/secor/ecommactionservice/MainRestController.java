package com.secor.ecommactionservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/action")
public class MainRestController {

    private final Logger LOG = LoggerFactory.getLogger(MainRestController.class);

    @Autowired
    private ActionRepository actionRepository;
    
    @Autowired
    private ActionService actionService;


    @PostMapping("/create/order")
    public ResponseEntity<?> createOrder(@RequestBody Order order) {
        LOG.info("createOrder({})", order);
        LOG.info("getProductDetails({})", order.getProductId());
        Product product = actionService.getProductDetails(order.getProductId());
        if (product == null) {
            LOG.error("Product not found for productId: {}", order.getProductId());
            return ResponseEntity.status(400).body("Product not found for productId: " + order.getProductId());
        }
        else{
            LOG.info("Product found: {}", product);
            InventoryItem inventoryItem = actionService.checkInventory(order.getProductId());
            if (inventoryItem == null) {
                LOG.error("No inventory found for productId: {}", order.getProductId());
                return ResponseEntity.status(400).body("No inventory found for productId: " + order.getProductId());
            }
            else if (inventoryItem.getQuantity() <= 0 || inventoryItem.getQuantity() < order.getQuantity()) {
                LOG.error("Insufficient inventory for productId: {}", order.getProductId());
                return ResponseEntity.status(400).body("Insufficient inventory for productId: " + order.getProductId());
            }
            else{
                LOG.info("Sufficient inventory found: {}", inventoryItem);
                order.setOrderDate(LocalDateTime.now());
                order.setUpdatedAt(LocalDateTime.now());
                order.setStatus("PENDING_PAYMENT");
                BigDecimal totalAmount = product.getPrice().multiply(BigDecimal.valueOf(order.getQuantity()));
                order.setTotalAmount(totalAmount);
                LOG.info("Save Order");
                Order savedOrder = actionService.saveOrder(order);
                LOG.info("created order: {}", savedOrder);
                LOG.info("create a payment for this order");
                Payment payment = new Payment();
                payment.setOrderId(savedOrder.getOrderId());
                payment.setCustomerId(savedOrder.getCustomerId());
                payment.setPaymentMethod("ONLINE");
                payment.setAmount(totalAmount);
                ResponseEntity<?> result = actionService.createPayment(payment);
                LOG.info("Payment created: {}", result);
                return ResponseEntity.ok("Order initiated successfully and created payment");
            }
        }
    }


    @PostMapping("/process/payment")
    public ResponseEntity<?> processPayment(@RequestBody PaymentStatus status) {
        LOG.info("processPayment({})", status);
        Payment payment = actionService.getPaymentByOrder(status.getOrderId());
        if (payment != null) {
            payment.setStatus(status.getStatus());
            payment.setUpdatedAt(LocalDateTime.now());
            Payment updatedPayment = actionService.savePayment(payment);
            LOG.info("Payment status updated to " + updatedPayment.getStatus() + " for order " + updatedPayment.getOrderId());
            if("COMPLETED".equalsIgnoreCase(updatedPayment.getStatus())) {
                LOG.info("Check inventory for order id: " + updatedPayment.getOrderId());
                InventoryItem inventoryItem = actionService.checkInventory(updatedPayment.getOrderId());
                Order orderdetails = actionService.getOrderDetails(updatedPayment.getOrderId());
                if (inventoryItem != null && inventoryItem.getQuantity() >= orderdetails.getQuantity() ) {
                    LOG.info("Update inventory for order id: " + updatedPayment.getOrderId());
                    inventoryItem.setQuantity(inventoryItem.getQuantity() - orderdetails.getQuantity());
                    inventoryItem.setLastUpdated(LocalDateTime.now());
                    InventoryItem updatedInventory = actionService.updateInventory(inventoryItem);
                    LOG.info("Inventory updated: " + updatedInventory);

                    LOG.info("Update order status to confirmed for " + updatedPayment.getOrderId());
                    OrderStatus orderStatus = new OrderStatus();
                    orderStatus.setOrderId(updatedPayment.getOrderId());
                    orderStatus.setStatus("CONFIRMED");
                    actionService.updateOrderStatus(orderStatus);
                    LOG.info("Order status updated as confirmed for " + orderdetails.getOrderId());

                    LOG.info("Add order for customer " + updatedPayment.getCustomerId());
                    OrderInfo orderInfo = new OrderInfo();
                    orderInfo.setOrderId(updatedPayment.getOrderId());
                    orderInfo.setCustomerId(updatedPayment.getCustomerId());
                    actionService.updateCustomerOrder(orderInfo);
                    LOG.info("Order added for customer " + updatedPayment.getCustomerId());
                    return ResponseEntity.ok("Payment processed successfully and order confirmed for " + updatedPayment.getOrderId());
                } else {
                    LOG.info("Insufficient inventory for order id: " + updatedPayment.getOrderId());
                    LOG.info("Update order status to cancelled for " + updatedPayment.getOrderId());
                    OrderStatus orderStatus = new OrderStatus();
                    orderStatus.setOrderId(updatedPayment.getOrderId());
                    orderStatus.setStatus("CANCELLED");
                    ResponseEntity<?> orderResponse = actionService.updateOrderStatus(orderStatus);
                    LOG.info("Order status updated as cancelled for " + orderdetails.getOrderId());

                    LOG.info("Update payment status as refunded for " + updatedPayment.getOrderId());
                    updatedPayment.setStatus("REFUND_INITIATED");
                    updatedPayment.setUpdatedAt(LocalDateTime.now());
                    actionService.savePayment(updatedPayment);
                    return ResponseEntity.status(400).body("Insufficient inventory for order " + updatedPayment.getOrderId() + ". Order cancelled and payment status updated to REFUND_INITIATED.");
                }
            }
            else if("FAILED".equalsIgnoreCase(updatedPayment.getStatus())) {
                LOG.info("update order status for " + updatedPayment.getOrderId());
                OrderStatus orderStatus = new OrderStatus();
                orderStatus.setOrderId(updatedPayment.getOrderId());
                orderStatus.setStatus("CANCELLED");
                actionService.updateOrderStatus(orderStatus);
                return ResponseEntity.ok("Payment failed for order " + updatedPayment.getOrderId() + ". Order status updated to CANCELLED.");
            }
        }
        return ResponseEntity.status(400).body("Payment not found for order " + status.getOrderId());
    }

}