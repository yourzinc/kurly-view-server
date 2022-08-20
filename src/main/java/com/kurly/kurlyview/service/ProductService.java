package com.kurly.kurlyview.service;

import com.kurly.kurlyview.domain.product.Product;
import com.kurly.kurlyview.dto.ProductListResponseDto;
import com.kurly.kurlyview.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;

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
}
