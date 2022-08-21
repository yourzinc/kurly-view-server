package com.kurly.kurlyview.dto;

import com.mongodb.lang.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestResponseDto {
    @Nullable
    private Boolean result;
    @Nullable
    private String message;
}
