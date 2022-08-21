package com.kurly.kurlyview.repository;

import com.kurly.kurlyview.domain.product.Product;
import com.kurly.kurlyview.domain.review.Review;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends MongoRepository<Review, String> {
    Optional<Review> findById(String id);
    Optional<Review> findByMemberId(String memberId);
}
