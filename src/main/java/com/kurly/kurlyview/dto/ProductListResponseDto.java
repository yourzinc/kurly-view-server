package com.kurly.kurlyview.dto;

import lombok.*;

import java.util.List;

@Builder
@Getter
public class ProductListResponseDto {
    private List<ProductPreview> products;

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProductPreview {
        private String id;
        private String title;
        private Integer price;
        private String photo;
    }

}
