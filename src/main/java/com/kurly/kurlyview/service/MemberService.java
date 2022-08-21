package com.kurly.kurlyview.service;

import com.kurly.kurlyview.domain.member.Member;
import com.kurly.kurlyview.dto.SignInRequestDto;
import com.kurly.kurlyview.dto.TokenResponseDto;
import com.kurly.kurlyview.repository.MemberRepository;
import com.kurly.kurlyview.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider tokenProvider;
    @Transactional
    public TokenResponseDto login(SignInRequestDto requestDto) {

        Member member = memberRepository.findByEmail(requestDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("가입된 이메일이 아닙니다."));

        if ( !requestDto.getPassword().equals(member.getPassword()))
            throw new IllegalArgumentException("ID/PW가 일치하지 않습니다");

        return TokenResponseDto.builder()
                .success(true)
                .accessToken(tokenProvider.createToken(member.getId(), member.getEmail()))
                .name(member.getName())
                .build();
    }
}
