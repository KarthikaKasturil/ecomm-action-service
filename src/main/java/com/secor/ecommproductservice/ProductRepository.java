package com.secor.ecommproductservice;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String>
{
    Product findByProductId(String productId);
    Product findByProductName(String productName);
}
