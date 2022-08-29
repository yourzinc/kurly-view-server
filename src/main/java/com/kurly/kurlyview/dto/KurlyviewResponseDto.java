package com.kurly.kurlyview.dto;

import com.kurly.kurlyview.domain.member.Member;
import lombok.*;

import java.util.List;

@Builder
@Getter
public class KurlyviewResponseDto {
    private List<Member.Kurlyview> kurlyviews;
}
