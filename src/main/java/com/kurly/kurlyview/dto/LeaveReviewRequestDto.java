package com.kurly.kurlyview.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaveReviewRequestDto {
    private String product_name;
    private Integer product_type;
    private Integer rating;
    private Integer fresh_score;
    private Integer taste_score;
    private Integer delivery_score;
    private String contents;
}
