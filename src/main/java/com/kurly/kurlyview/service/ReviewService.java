package com.kurly.kurlyview.service;

import com.kurly.kurlyview.domain.member.Member;
import com.kurly.kurlyview.domain.product.Rate;
import com.kurly.kurlyview.domain.review.Review;
import com.kurly.kurlyview.dto.LeaveReviewRequestDto;
import com.kurly.kurlyview.dto.MontlyRateResponseDto;
import com.kurly.kurlyview.dto.UserReviewListResponseDto;
import com.kurly.kurlyview.repository.MemberRepository;
import com.kurly.kurlyview.repository.ProductRepository;
import com.kurly.kurlyview.repository.ReviewRepository;
import com.kurly.kurlyview.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
@Service
@Slf4j
public class ReviewService {
    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
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
                    .date(LocalDateTime.now())
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
                    .date(LocalDateTime.now())
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

    public MontlyRateResponseDto findMontlyRate(String productId) {

        int product_type = productRepository.findById(productId).get().getType();

        if (product_type == 0) {
            return nonFreshProductMontlyRate(productId);
        }
        else if (product_type == 1) {
            return freshProductMontlyRate(productId);
        }
        return MontlyRateResponseDto.builder().build();
    }

    public MontlyRateResponseDto nonFreshProductMontlyRate(String productId)
    {
        List<Rate> rating = new ArrayList<>();

        LocalDateTime now = LocalDateTime.now();
        Rate m1 = new Rate(now, 0.0);
        AtomicInteger c1 = new AtomicInteger();
        Rate m2 = new Rate(now.minusMonths(1), 0.0);
        AtomicInteger c2 = new AtomicInteger();
        Rate m3 = new Rate(now.minusMonths(2), 0.0);
        AtomicInteger c3 = new AtomicInteger();
        Rate m4 = new Rate(now.minusMonths(3), 0.0);
        AtomicInteger c4 = new AtomicInteger();

        log.info(reviewRepository.findAllByProductId(productId).toString());

        reviewRepository.findAllByProductId(productId).stream()
                .forEach(review -> {
                    Month review_month = review.getDate().getMonth();
                    if (review_month.equals(m1.getDate().getMonth())) {
                        m1.setRate(m1.getRate() + review.getRating());
                        c1.getAndIncrement();
                    } else if (review_month.equals(m2.getDate().getMonth())) {
                        m2.setRate(m2.getRate()+review.getRating());
                        c2.getAndIncrement();
                    } else if (review_month.equals(m3.getDate().getMonth())) {
                        m3.setRate(m3.getRate()+review.getRating());
                        c3.getAndIncrement();
                    } else if (review_month.equals(m4.getDate().getMonth())) {
                        m4.setRate(m4.getRate()+review.getRating());
                        c4.getAndIncrement();
                    }
                });

        if (c1.get() != 0) m1.setRate(m1.getRate()/c1.get());  rating.add(m1);
        if (c2.get() != 0) m2.setRate(m2.getRate()/c2.get());  rating.add(m2);
        if (c3.get() != 0) m3.setRate(m3.getRate()/c3.get());  rating.add(m3);
        if (c4.get() != 0) m4.setRate(m4.getRate()/c4.get());  rating.add(m4);

        log.info(rating.toString());
        return MontlyRateResponseDto.builder()
                .rating(rating)
                .build();
    }

    public MontlyRateResponseDto freshProductMontlyRate(String productId)
    {
        List<Rate> rating = new ArrayList<>();
        List<Rate> fresh_score = new ArrayList<>();
        List<Rate> taste_score = new ArrayList<>();
        List<Rate> delivery_score = new ArrayList<>();

        // rating
        LocalDateTime now = LocalDateTime.now();
        Rate m1 = new Rate(now, 0.0);
        AtomicInteger c1 = new AtomicInteger();
        Rate m2 = new Rate(now.minusMonths(1), 0.0);
        AtomicInteger c2 = new AtomicInteger();
        Rate m3 = new Rate(now.minusMonths(2), 0.0);
        AtomicInteger c3 = new AtomicInteger();
        Rate m4 = new Rate(now.minusMonths(3), 0.0);
        AtomicInteger c4 = new AtomicInteger();

        // fresh_score
        Rate f1 = new Rate(now, 0.0);
        Rate f2 = new Rate(now.minusMonths(1), 0.0);
        Rate f3 = new Rate(now.minusMonths(2), 0.0);
        Rate f4 = new Rate(now.minusMonths(3), 0.0);

        // taste_score
        Rate t1 = new Rate(now, 0.0);
        Rate t2 = new Rate(now.minusMonths(1), 0.0);
        Rate t3 = new Rate(now.minusMonths(2), 0.0);
        Rate t4 = new Rate(now.minusMonths(3), 0.0);

        // delivery_score
        Rate d1 = new Rate(now, 0.0);
        Rate d2 = new Rate(now.minusMonths(1), 0.0);
        Rate d3 = new Rate(now.minusMonths(2), 0.0);
        Rate d4 = new Rate(now.minusMonths(3), 0.0);

        reviewRepository.findAllByProductId(productId).stream()
                .forEach(review -> {
                    Month review_month = review.getDate().getMonth();
                    if (review_month.equals(m1.getDate().getMonth())) {
                        m1.setRate(m1.getRate() + review.getRating());
                        f1.setRate(f1.getRate()+ review.getFreshScore());
                        t1.setRate(t1.getRate()+ review.getTasteScore());
                        d1.setRate(d1.getRate()+ review.getDeliveryScore());
                        c1.getAndIncrement();
                    } else if (review_month.equals(m2.getDate().getMonth())) {
                        m2.setRate(m2.getRate()+review.getRating());
                        f2.setRate(f2.getRate()+ review.getFreshScore());
                        t2.setRate(t2.getRate()+ review.getTasteScore());
                        d2.setRate(d2.getRate()+ review.getDeliveryScore());
                        c2.getAndIncrement();
                    } else if (review_month.equals(m3.getDate().getMonth())) {
                        m3.setRate(m3.getRate()+review.getRating());
                        f3.setRate(f3.getRate()+ review.getFreshScore());
                        t3.setRate(t3.getRate()+ review.getTasteScore());
                        d3.setRate(d3.getRate()+ review.getDeliveryScore());
                        c3.getAndIncrement();
                    } else if (review_month.equals(m4.getDate().getMonth())) {
                        m4.setRate(m4.getRate()+review.getRating());
                        f4.setRate(f4.getRate()+ review.getFreshScore());
                        t4.setRate(t4.getRate()+ review.getTasteScore());
                        d4.setRate(d4.getRate()+ review.getDeliveryScore());
                        c4.getAndIncrement();
                    }
                });

        if (c1.get() != 0){
            m1.setRate(m1.getRate()/c1.get());
            f1.setRate(f1.getRate()/c1.get());
            t1.setRate(t1.getRate()/c1.get());
            d1.setRate(d1.getRate()/c1.get());
        }
        if (c2.get() != 0){
            m2.setRate(m2.getRate()/c2.get());
            f2.setRate(f2.getRate()/c2.get());
            t2.setRate(t2.getRate()/c2.get());
            d2.setRate(d2.getRate()/c2.get());
        }
        if (c3.get() != 0) {
            m3.setRate(m3.getRate()/c3.get());
            f3.setRate(f3.getRate()/c3.get());
            t3.setRate(t3.getRate()/c3.get());
            d3.setRate(d3.getRate()/c3.get());
        }
        if (c4.get() != 0){
            m4.setRate(m4.getRate()/c4.get());
            f4.setRate(f4.getRate()/c4.get());
            t4.setRate(t4.getRate()/c4.get());
            d4.setRate(d4.getRate()/c4.get());
        }

        for (Rate m : new Rate[]{m1, m2, m3, m4})
            rating.add(m);
        for (Rate f : new Rate[]{f1, f2, f3, f4})
            fresh_score.add(f);
        for (Rate t :new Rate[]{t1, t2, t3, t4})
            taste_score.add(t);
        for (Rate d : new Rate[]{d1, d2, d3, d3})
            delivery_score.add(d);

        log.info(rating.toString());
        log.info(fresh_score.toString());
        log.info(taste_score.toString());
        log.info(delivery_score.toString());

        return MontlyRateResponseDto.builder()
                .rating(rating)
                .fresh_score(fresh_score)
                .taste_score(taste_score)
                .delivery_score(delivery_score)
                .build();
    }

//    public Object findWeeklyRate(String productId) {
//    }
}
