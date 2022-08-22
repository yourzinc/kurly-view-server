package com.kurly.kurlyview.repository;

import com.kurly.kurlyview.domain.product.Product;
import com.kurly.kurlyview.domain.review.Review;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends MongoRepository<Review, String> {
    Optional<Review> findById(String id);
    List<Review> findByMemberId(@Param("member_id") String memberId);
    List<Review> findAllByProductId(@Param("product_id") String productId);

    List<Review> findAllByMemberIdAndRating(@Param("member_id") String memberId,
                                            @Param("rating") Integer rating);
}
