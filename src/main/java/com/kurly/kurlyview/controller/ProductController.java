package com.kurly.kurlyview.controller;

import com.kurly.kurlyview.domain.product.Product;
import com.kurly.kurlyview.dto.LeaveReviewRequestDto;
import com.kurly.kurlyview.dto.ProductListResponseDto;
import com.kurly.kurlyview.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "/api")
@RestController
public class ProductController {

    private final ProductService productService;

    @GetMapping("/products")
    public ProductListResponseDto getProductList(){
        return productService.findProductPreview();
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<?> getProductDetail(@PathVariable String productId){
        return ResponseEntity.ok(productService.findProductDetail(productId));
    }

    /**
     * 리뷰 작성하기
     */
    @PostMapping("/products/{productId}/reviews")
    public ResponseEntity<?> postReview(@RequestHeader("X-ACCESS-TOKEN") String token,
                                        @PathVariable String productId,
                                        @RequestBody LeaveReviewRequestDto dto){
        return ResponseEntity.ok(productService.leaveReview(token, productId, dto));
    }
}
