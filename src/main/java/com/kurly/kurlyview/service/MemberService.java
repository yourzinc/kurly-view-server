package com.kurly.kurlyview.service;

import com.kurly.kurlyview.domain.member.Member;
import com.kurly.kurlyview.dto.KurlyviewSubscribeRequestDto;
import com.kurly.kurlyview.dto.SignInRequestDto;
import com.kurly.kurlyview.dto.TestResponseDto;
import com.kurly.kurlyview.dto.TokenResponseDto;
import com.kurly.kurlyview.repository.MemberRepository;
import com.kurly.kurlyview.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
    @Transactional
    public TestResponseDto subscribe(String token, KurlyviewSubscribeRequestDto dto) {

        String email = tokenProvider.getUserEmail(token);

        // 이메일 NULL 처리
        if (email == null) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("가입된 이메일이 아닙니다."));

        List<Member.Kurlyview> kurlyviews = member.getKurlyviews();

        Optional<Member> new_kurlyview = memberRepository.findById(dto.getId());

        System.out.println(new_kurlyview);

        kurlyviews.add(Member.Kurlyview.builder()
                .name(new_kurlyview.get().getName())
                .email(new_kurlyview.get().getEmail())
                .build());

        memberRepository.save(member);

        return TestResponseDto.builder()
                .success(true)
                .build();
    }
}
