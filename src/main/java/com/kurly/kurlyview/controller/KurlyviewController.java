package com.kurly.kurlyview.controller;

import com.kurly.kurlyview.dto.KurlyviewSubscribeRequestDto;
import com.kurly.kurlyview.dto.SignInRequestDto;
import com.kurly.kurlyview.service.MemberService;
import com.kurly.kurlyview.service.RecommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Stack;

@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "/api")
@RestController
public class KurlyviewController {
    private final MemberService memberService;
    private final RecommandService recommandService;

    @GetMapping("/kurlyview-reviews")
    public ResponseEntity<?> getKurlyviews(@RequestHeader("Authorization") String token) {
        System.out.println("getKurlyviews");
        return ResponseEntity.ok(memberService.findAllKurlyview(token));
    }

    @GetMapping("/kurlyviews/{memberId}")
    public ResponseEntity<?> subscribeKurlyview(@RequestHeader("Authorization") String token, @PathVariable String memberId){
        return ResponseEntity.ok(memberService.subscribe(token, memberId));
    }

    @DeleteMapping("/kurlyviews/{memberId}")
    public ResponseEntity<?> unsubscribeKurlyview(@RequestHeader("Authorization") String token, @PathVariable String memberId){
        return ResponseEntity.ok(memberService.unsubscribe(token, memberId));
    }

    @GetMapping("/kurlyviews/{memberId}/status")
    public ResponseEntity<?> getSubscribeStatus(@RequestHeader("Authorization") String token, @PathVariable String memberId){
        return ResponseEntity.ok(memberService.subscribeStatue(token, memberId));
    }

    /**
     * 컬리뷰 상품 추천 목록
     */
    @GetMapping("/kurlyview-products")
    public ResponseEntity<?> getKurlyviewRecommand(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(recommandService.recommandProduct(token));
    }
}
