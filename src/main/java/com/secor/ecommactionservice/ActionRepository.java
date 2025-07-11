package com.secor.ecommactionservice;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ActionRepository extends MongoRepository<Product, String>
{

}
