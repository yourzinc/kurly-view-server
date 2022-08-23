package com.kurly.kurlyview.service;

import com.kurly.kurlyview.domain.member.Member;
import com.kurly.kurlyview.domain.review.Review;
import com.kurly.kurlyview.dto.LeaveReviewRequestDto;
import com.kurly.kurlyview.dto.ReviewListResponseDto;
import com.kurly.kurlyview.dto.UserReviewListResponseDto;
import com.kurly.kurlyview.repository.MemberRepository;
import com.kurly.kurlyview.repository.ReviewRepository;
import com.kurly.kurlyview.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ReviewService {
    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;
    private final JwtTokenProvider tokenProvider;

    /**
     * 리뷰 작성
     */
    @Transactional
    public Review leaveReview(String token, String product_id, LeaveReviewRequestDto dto) {
        // token 으로 member_id , member_name 확인
        String id = tokenProvider.getUserIdFromJWT(token);

        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("가입된 이메일이 아닙니다."));

        // type = 0 이면 일반 식품 -> rating 만
        // type = 1 이면 신선 식품 -> + fresh_score, taste_score, delivery_score 추가
        Integer product_type = dto.getProduct_type();
        Review review;

        if ( product_type == 0 ) {
            review = Review.builder()
                    .memberId(member.getId())
                    .memberName(member.getName())
                    .productId(product_id)
                    .productName(dto.getProduct_name())
                    .productType(product_type)
                    .rating(dto.getRating())
                    .content(dto.getContents())
                    .photo("https://res.cloudinary.com/rebelwalls/image/upload/b_black,c_fill,e_blur:2000,f_auto,fl_progressive,h_533,q_auto,w_800/v1479371015/article/R10921_image1")
                    .date(new Date())
                    .build();
        }
        else {
            review = Review.builder()
                    .memberId(member.getId())
                    .memberName(member.getName())
                    .productId(product_id)
                    .productName(dto.getProduct_name())
                    .productType(product_type)
                    .rating(dto.getRating())
                    .freshScore(dto.getFresh_score())
                    .tasteScore(dto.getTaste_score())
                    .deliveryScore(dto.getDelivery_score())
                    .content(dto.getContents())
                    .photo("https://res.cloudinary.com/rebelwalls/image/upload/b_black,c_fill,e_blur:2000,f_auto,fl_progressive,h_533,q_auto,w_800/v1479371015/article/R10921_image1")
                    .date(new Date())
                    .build();
        }

        reviewRepository.save(review);

        return review;
    }

    /**
     * 상품의 리뷰 전체
     */
    public List<Review> findProductReviews(String productId) {
        return reviewRepository.findAllByProductId(productId);
    }

    /**
     * 사용자의 리뷰 전체
     */
    public UserReviewListResponseDto findUserReviews(String memberId) {

        // token 으로 로그인한 회원인지 확인
        // 구독중인 kurlyview 인지 확인하기
        // token 이 없거나 유효하지 않다면 -> 구독중이 아니다 라고 처리

//        boolean is_follow = false;
//
//        if (token != null) {
//            is_follow = memberRepository.findById(tokenProvider.getUserId(token)).get()
//                    .getKurlyviews().stream()
//                    .anyMatch(kurlyview -> kurlyview.equals(memberId));
//        }


        // memberId가 작성한 리뷰 검색
        return UserReviewListResponseDto.builder()
                .name(memberRepository.findById(memberId).get().getName())
                .reviews(reviewRepository.findByMemberId(memberId))
                .build();
    }
}
