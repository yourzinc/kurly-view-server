package com.kurly.kurlyview.service;

import com.kurly.kurlyview.domain.member.Member;
import com.kurly.kurlyview.domain.product.Product;
import com.kurly.kurlyview.domain.review.Review;
import com.kurly.kurlyview.dto.LeaveReviewRequestDto;
import com.kurly.kurlyview.dto.ProductListResponseDto;
import com.kurly.kurlyview.repository.MemberRepository;
import com.kurly.kurlyview.repository.ProductRepository;
import com.kurly.kurlyview.repository.ReviewRepository;
import com.kurly.kurlyview.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;

    private final JwtTokenProvider tokenProvider;

    /**
     * 상품 목록 전체
     */
    public ProductListResponseDto findProductPreview(){

        List<ProductListResponseDto.ProductPreview> product_previews = new ArrayList<ProductListResponseDto.ProductPreview>();

        List<Product> products = productRepository.findAll();

        for (Product product : products ) {
            ProductListResponseDto.ProductPreview product_preview = ProductListResponseDto.ProductPreview.builder()
                    .id(product.getId())
                    .title(product.getTitle())
                    .price(product.getPrice())
                    .photo(product.getPhoto())
                    .build();

            product_previews.add(product_preview);
        }

        // if product_previews isEmpty()

        return ProductListResponseDto.builder()
                .products(product_previews)
                .build();
    }

    /**
     * 상품 상세 설명
     */
    public Optional<Product> findProductDetail(String id)
    {
        return productRepository.findById(id);
    }

    /**
     * 리뷰 작성
     */
    @Transactional
    public Review leaveReview(String token, String product_id, LeaveReviewRequestDto dto) {
        // token 으로 member_id , member_name 확인
        String email = tokenProvider.getUserEmail(token);

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("가입된 이메일이 아닙니다."));

        // type = 0 이면 일반 식품 -> rating 만
        // type = 1 이면 신선 식품 -> + fresh_score, taste_score, delivery_score 추가
        Integer product_type = dto.getProduct_type();
        Review review;

        if ( product_type == 0 ) {
            review = Review.builder()
                    .memberId(email)
                    .memberName(member.getName())
                    .productId(product_id)
                    .productName(dto.getProduct_name())
                    .productType(product_type)
                    .rating(dto.getRating())
                    .content(dto.getContents())
                    .photo("https://res.cloudinary.com/rebelwalls/image/upload/b_black,c_fill,e_blur:2000,f_auto,fl_progressive,h_533,q_auto,w_800/v1479371015/article/R10921_image1")
                    .date(new Date())
                    .build();
        }
        else {
            review = Review.builder()
                    .memberId(email)
                    .memberName(member.getName())
                    .productId(product_id)
                    .productName(dto.getProduct_name())
                    .productType(product_type)
                    .rating(dto.getRating())
                    .freshScore(dto.getFresh_score())
                    .tasteScore(dto.getTaste_score())
                    .deliveryScore(dto.getDelivery_score())
                    .content(dto.getContents())
                    .photo("https://res.cloudinary.com/rebelwalls/image/upload/b_black,c_fill,e_blur:2000,f_auto,fl_progressive,h_533,q_auto,w_800/v1479371015/article/R10921_image1")
                    .date(new Date())
                    .build();
        }

        reviewRepository.save(review);

        return review;
    }

    /**
     * 상품의 리뷰 전체
     */
    public List<Review> findProductReviews(String productId) {
        return reviewRepository.findAllByProductId(productId);
    }
}
