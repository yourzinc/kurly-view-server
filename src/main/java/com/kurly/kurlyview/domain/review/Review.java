package com.kurly.kurlyview.domain.review;

import com.kurly.kurlyview.domain.product.Detail;
import com.mongodb.lang.Nullable;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Data
@Document(collection = "Review")
public class Review {
    @Id
    private String id;

    private String memberId;
    private String memberName;

    private String productId;
    private String productName;
    private Integer productType;

    private Integer rating;
    @Nullable
    private Integer freshScore;
    @Nullable
    private Integer tasteScore;
    @Nullable
    private Integer deliveryScore;

    private String content;
    @Nullable
    private String photo;
    private Date date;
}
