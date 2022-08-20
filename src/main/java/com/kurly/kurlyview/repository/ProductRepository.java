package com.kurly.kurlyview.repository;

import com.kurly.kurlyview.domain.product.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ProductRepository extends MongoRepository<Product, String> {
    Optional<Product> findById(String id);
}
