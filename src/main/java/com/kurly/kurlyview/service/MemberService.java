package com.kurly.kurlyview.service;

import com.kurly.kurlyview.domain.member.Member;
import com.kurly.kurlyview.domain.review.Review;
import com.kurly.kurlyview.dto.*;
import com.kurly.kurlyview.repository.MemberRepository;
import com.kurly.kurlyview.repository.ReviewRepository;
import com.kurly.kurlyview.security.jwt.JwtTokenProvider;
import com.kurly.kurlyview.security.jwt.UserAuthentication;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.SpringTransactionAnnotationParser;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;
    private final JwtTokenProvider tokenProvider;

    @Transactional
    public TokenResponseDto login(SignInRequestDto requestDto) {

        Member member = memberRepository.findByEmail(requestDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("가입된 이메일이 아닙니다."));

        if ( !requestDto.getPassword().equals(member.getPassword()))
            throw new IllegalArgumentException("ID/PW가 일치하지 않습니다");

        Authentication authentication = new UserAuthentication(member.getId(), null, null);

        return TokenResponseDto.builder()
                .success(true)
                .accessToken(tokenProvider.generateToken(authentication))
                .name(member.getName())
                .build();
    }
    @Transactional
    public TestResponseDto subscribe(String token, String id) {

        String _id = tokenProvider.getUserIdFromJWT(token);

        // 이메일 NULL 처리
        if (_id == null) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        Member member = memberRepository.findById(_id)
                .orElseThrow(() -> new IllegalArgumentException("가입된 이메일이 아닙니다."));

        List<Member.Kurlyview> kurlyviews = member.getKurlyviews();

        if (kurlyviews != null)
            if (kurlyviews.stream().anyMatch(k -> k.getId().equals(id)) == true)
                return TestResponseDto.builder()
                        .message("이미 구독중인 컬리뷰입니다.")
                        .build();

        Optional<Member> new_kurlyview = memberRepository.findById(id);

        kurlyviews.add(Member.Kurlyview.builder()
                .id(new_kurlyview.get().getId())
                .name(new_kurlyview.get().getName())
                .email(new_kurlyview.get().getEmail())
                .build());

        memberRepository.save(member);

        return TestResponseDto.builder()
                .message("success")
                .build();
    }

    @Transactional
    public Object unsubscribe(String token, String id) {
        String _id = tokenProvider.getUserIdFromJWT(token);

        // 이메일 NULL 처리
        if (_id == null) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        Member member = memberRepository.findById(_id)
                .orElseThrow(() -> new IllegalArgumentException("가입된 이메일이 아닙니다."));

        List<Member.Kurlyview> kurlyviews = member.getKurlyviews();

        if (kurlyviews == null)
            throw new IllegalArgumentException("구독 중인 컬리뷰가 존재하지 않습니다.");

        for (Member.Kurlyview kurlyview : kurlyviews)
            if (kurlyview.getId().equals(id)) {
                kurlyviews.remove(kurlyview);
                memberRepository.save(member);

                return TestResponseDto.builder()
                        .message("success")
                        .build();
            }

        return TestResponseDto.builder()
                .message("구독중인 컬리뷰가 아닙니다.")
                .build();
    }

    @Transactional
    public Object subscribeStatue(String token, String id)
    {
        return TestResponseDto.builder()
                .result(isfollowing(token, id))
                .build();
    }
    public boolean isfollowing(String token, String id) {
        boolean is_follow = false;

        String _id = tokenProvider.getUserIdFromJWT(token);

        // 이메일 NULL 처리
        if (_id == null) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        Member member = memberRepository.findById(_id)
                .orElseThrow(() -> new IllegalArgumentException("가입된 이메일이 아닙니다."));

        List<Member.Kurlyview> kurlyviews = member.getKurlyviews();

        if (kurlyviews == null) {
            kurlyviews = new ArrayList<>();
        }

        if (kurlyviews.stream().anyMatch(kurlyview -> kurlyview.getId().equals(id)))
            is_follow = true;

        return is_follow;
    }

    public ReviewListResponseDto findAllKurlyview(String token) {
        List<Review> reviews = new ArrayList<Review>();

        String _id = tokenProvider.getUserIdFromJWT(token);
        System.out.println("is-following : " + _id);

        // 이메일 NULL 처리
        if (_id == null) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        System.out.println("_id : " + _id);
        Member member = memberRepository.findById(_id)
                .orElseThrow(() -> new IllegalArgumentException("가입된 ID이 아닙니다."));

        List<Member.Kurlyview> kurlyviews = member.getKurlyviews();

        if (kurlyviews == null) {
            kurlyviews = new ArrayList<>();
        }
        else {
            kurlyviews.stream().forEach(kurlyview ->
                    reviews.addAll(reviewRepository.findByMemberId(kurlyview.getId()))
            );
        }

        reviews.sort(Comparator.comparing(Review::getDate).reversed());

        return ReviewListResponseDto.builder()
                .reviews(reviews)
                .build();
    }
}
