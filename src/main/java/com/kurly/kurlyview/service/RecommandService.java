package com.kurly.kurlyview.service;

import com.kurly.kurlyview.domain.product.Product;
import com.kurly.kurlyview.dto.ProductListResponseDto;
import com.kurly.kurlyview.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class RecommandService {
    public ProductListResponseDto recommandProduct(String token) {

        List<Product> recommand_products = new ArrayList<>();
        List<ProductListResponseDto.ProductPreview> recommand_products_preview = new ArrayList<>();

        // 구독하고 있는 컬리뷰 id list

        // list 에 개별로 접근하여 Review 에서 memberId + rating = 5점 검색

        // return
        return ProductListResponseDto.builder()
                .products(recommand_products_preview)
                .build();
    }
}
