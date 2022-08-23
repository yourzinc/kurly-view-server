package com.kurly.kurlyview.dto;

import com.kurly.kurlyview.domain.product.Rate;
import com.mongodb.lang.Nullable;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ReviewRateResponseDto {
    private List<Rate> rating;
    @Nullable
    private List<Rate> fresh_score;
    @Nullable
    private List<Rate> taste_score;
    @Nullable
    private List<Rate> delivery_score;
}

