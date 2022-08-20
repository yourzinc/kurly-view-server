package com.kurly.kurlyview.controller;

import com.kurly.kurlyview.dto.ProductListResponseDto;
import com.kurly.kurlyview.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "/api")
@RestController
public class ProductController {

    private final ProductService productService;

    @GetMapping("/products")
    public ProductListResponseDto listAll(){
        return productService.findProductPreview();
    }

}
