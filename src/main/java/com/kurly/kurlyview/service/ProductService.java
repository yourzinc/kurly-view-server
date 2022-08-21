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
}
