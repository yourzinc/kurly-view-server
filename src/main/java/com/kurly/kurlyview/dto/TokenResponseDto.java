package com.kurly.kurlyview.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenResponseDto {
    private boolean success;
    private String accessToken;
    // private String refreshToken;
    private String name;
}
