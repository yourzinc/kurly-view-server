package com.kurly.kurlyview.service;

import com.kurly.kurlyview.domain.member.Member;
import com.kurly.kurlyview.domain.product.Product;
import com.kurly.kurlyview.dto.ProductListResponseDto;
import com.kurly.kurlyview.repository.MemberRepository;
import com.kurly.kurlyview.repository.ProductRepository;
import com.kurly.kurlyview.repository.ReviewRepository;
import com.kurly.kurlyview.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class RecommandService {

    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;
    private final JwtTokenProvider tokenProvider;
    public ProductListResponseDto recommandProduct(String token) {

        List<String> recommand_products = new ArrayList<>(); // 컬리뷰의 리뷰 5점 상품들 id
        List<ProductListResponseDto.ProductPreview> recommand_products_preview  = new ArrayList<>();

        // 구독하고 있는 컬리뷰 id list
        String _id = tokenProvider.getUserIdFromJWT(token);

        // 이메일 NULL 처리
        if (_id == null) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        Member member = memberRepository.findById(_id)
                .orElseThrow(() -> new IllegalArgumentException("가입된 이메일이 아닙니다."));

        // list 에 개별로 접근하여 Review 에서 memberId + rating = 5점 검색
        List<Member.Kurlyview> kurlyviews = member.getKurlyviews();

        if (kurlyviews != null) {
            kurlyviews.stream()
                    .forEach(kurlyview -> {
                        log.info("kurlyviews - "+ kurlyview.getId());
                        reviewRepository.findAllByMemberIdAndRating(kurlyview.getId(), 5).stream()
                                .forEach(review -> {
                                            recommand_products.add(review.getProductId());
                                        }
                        );
            });
        }

        log.info(recommand_products.toString());

        recommand_products.stream().distinct().forEach( product_id -> {
            Product product = productRepository.findById(product_id).get();
            recommand_products_preview.add(
                    ProductListResponseDto.ProductPreview.builder()
                            .title(product.getTitle())
                            .id(product.getId())
                            .photo(product.getPhoto())
                            .price(product.getPrice())
                            .build()
            );
        });

        // return
        return ProductListResponseDto.builder()
                .products(recommand_products_preview)
                .build();
    }
}
