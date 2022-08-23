package com.kurly.kurlyview.dto;

import com.kurly.kurlyview.domain.review.Review;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class UserReviewListResponseDto {
    private String name;
    private List<Review> reviews;
}

