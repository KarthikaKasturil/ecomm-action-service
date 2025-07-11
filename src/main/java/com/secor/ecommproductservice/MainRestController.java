package com.secor.ecommproductservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class MainRestController {

    private final Logger LOG = LoggerFactory.getLogger(MainRestController.class);

    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public List<Product> getAllProducts() {
        LOG.info("getAllProducts");
        return productRepository.findAll();
    }

    @GetMapping("/{productId}")
    public Product getProductByProductId(@PathVariable String productId) {
        LOG.info("getProductByProductId({})", productId);
        return productRepository.findByProductId(productId);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addProduct(@RequestBody Product product) {
        LOG.info("addProduct({})", product);
        Product existing = productRepository.findByProductName(product.getProductName());
        LOG.info("Product exists: {}", existing != null);
        if (existing != null) {
            return ResponseEntity.status(400).body("Product already exists");
        }
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        Product savedProduct = productRepository.save(product);
        return ResponseEntity.ok("Product " + savedProduct.getProductName() + " added to catalog - " + savedProduct.getProductId());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable String id, @RequestBody Product productDetails) {
        LOG.info("updateProduct({})", productDetails);
        Optional<Product> product = productRepository.findById(id);
        if (product.isEmpty()) {
            return ResponseEntity.status(400).body("No such product");
        }
        Product existingProduct = product.get();
        existingProduct.setProductName(productDetails.getProductName());
        existingProduct.setDescription(productDetails.getDescription());
        existingProduct.setPrice(productDetails.getPrice());
        existingProduct.setCategory(productDetails.getCategory());
        existingProduct.setUpdatedAt(LocalDateTime.now());
        Product savedProduct = productRepository.save(existingProduct);
        return ResponseEntity.ok("Product " + savedProduct.getProductName() + " updated in catalog - " + savedProduct.getProductId());
    }

}