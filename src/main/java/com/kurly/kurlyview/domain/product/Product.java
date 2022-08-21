package com.kurly.kurlyview.domain.product;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Data
@Document(collection = "Product")
public class Product {
    @Id
    private String id;
    private String title;
    private Integer type;
    private Integer price;
    private String photo;
    private String h1;
    private List<Detail> details;
    private String product_photo;
    private String detailed_photo;
}
